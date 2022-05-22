package com.example.android.goalchaser.utils.uiutils

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.activecompletedgoals.ActiveCompletedGoalViewModel
import com.example.android.goalchaser.ui.activecompletedgoals.ActiveGoalsListFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
/**This class solves the issue where AlertDialog is dismissed on device rotation as it is
 *lifecycle-aware*/
class GoalCompletionDialogFragment : DialogFragment() {
    private val viewModel: ActiveCompletedGoalViewModel by inject()
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
                getString(
                    R.string.alert_dialog_mark_completed,
                    goalTitle
                )
            )
            .setMessage(R.string.alert_dialog_message)
            .setPositiveButton(R.string.alert_dialog_confirm_completed) { dialog: DialogInterface, _ ->
                viewModel.markGoalCompleted(goalId)
                viewModel.setCompletedGoalTitle(goalTitle)
                dialog.dismiss()
                findNavController().navigate(
                    ActiveGoalsListFragmentDirections
                        .actionActiveGoalsFragmentToGoalCompletedFragment()
                )
            }
            .setNegativeButton(R.string.alert_dialog_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        return dialog
    }


    companion object {
        const val TAG = "GoalCompletionDialog"
        const val GOAL_ID = "GOAL_ID"
        const val GOAL_TITLE = "GOAL_TITLE"
    }

    fun setupGoalCompletionDialog(
        completedGoalId: Int,
        completedGoalTitle: String?
    ): GoalCompletionDialogFragment {
        goalId = completedGoalId
        goalTitle = completedGoalTitle
        return this
    }
}
