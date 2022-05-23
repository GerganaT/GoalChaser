package com.example.android.goalchaser.utils.uiutils

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.activecompletedgoals.ActiveCompletedGoalsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject

/**This class solves the issue where AlertDialog is dismissed on device rotation as it is
 *lifecycle-aware*/

class GoalDeletionDialogFragment
    : DialogFragment() {
    private val viewModel: ActiveCompletedGoalsViewModel by inject()
    private var goalId: Int = 0
    private var goalTitle: String? = ""
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putInt(GOAL_ID, goalId)
            putString(GOAL_TITLE, goalTitle)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        savedInstanceState?.run {
            goalId = getInt(GOAL_ID)
            goalTitle = getString(GOAL_TITLE)
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                getString(R.string.alert_dialog_title, goalTitle)
            )
            .setMessage(R.string.alert_dialog_message)
            .setPositiveButton(R.string.alert_dialog_delete) { _, _ ->
                viewModel.deleteGoal(goalId)
                Toast.makeText(
                    context,
                    getString(R.string.deleted_goal_toast, goalTitle),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.refreshGoals()
            }
            .setNegativeButton(R.string.alert_dialog_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        return dialog
    }


    companion object {
        const val TAG = "GoalDeletionDialog"
        const val GOAL_ID = "GOAL_ID"
        const val GOAL_TITLE = "GOAL_TITLE"
    }

    fun setupGoalDeleteDialog(
        deletedGoalId: Int,
        deletedGoalTitle: String?
    ): GoalDeletionDialogFragment {
        goalId = deletedGoalId
        goalTitle = deletedGoalTitle
        return this

    }
}
