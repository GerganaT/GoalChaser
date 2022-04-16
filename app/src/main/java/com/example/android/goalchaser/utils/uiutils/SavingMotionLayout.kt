package com.example.android.goalchaser.utils.uiutils

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout

// code from:
//https://stackoverflow.com/questions/65048418/
// how-to-restore-transition-state-of-motionlayout-without-auto-playing-the-transit

/** This class solves an issue where the goal notification controls animation state is
 * not properly persisted throughout configuration changes such as device rotation*/
class SavingMotionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    override fun onSaveInstanceState(): Parcelable {
        return SaveState(super.onSaveInstanceState(), startState, endState, targetPosition)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? SaveState)?.let {
            super.onRestoreInstanceState(it.superParcel)
            setTransition(it.startState, it.endState)
            progress = it.progress
        }
    }

    @kotlinx.android.parcel.Parcelize
    private class SaveState(
        val superParcel: Parcelable?,
        val startState: Int,
        val endState: Int,
        val progress: Float
    ) : Parcelable
}