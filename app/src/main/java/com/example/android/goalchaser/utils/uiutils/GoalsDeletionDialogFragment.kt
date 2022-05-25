package com.example.android.goalchaser.utils.uiutils

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.activecompletedgoals.ActiveCompletedGoalsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject

/**This class solves the issue where AlertDialog is dismissed on device rotation as it is
 *lifecycle-aware*/

class GoalsDeletionDialogFragment
    : DialogFragment() {
    private val viewModel: ActiveCompletedGoalsViewModel by inject()
    private var menuSelection: MenuSelection = MenuSelection.DELETE_ACTIVE_GOALS
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putSerializable(MENU_SELECTION, menuSelection)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        savedInstanceState?.run {
            menuSelection = getSerializable(MENU_SELECTION) as MenuSelection
        }
                val dialogTitle = when (menuSelection) {
                    MenuSelection.DELETE_ACTIVE_GOALS -> R.string.alert_dialog_delete_all_active_goals
                    MenuSelection.DELETE_COMPLETED_GOALS -> R.string.alert_dialog_delete_all_completed_goals
                    MenuSelection.DELETE_ALL_GOALS -> R.string.alert_dialog_delete_all_goals
                    else -> 0
                }
                val deletedGoalsToastText = when(menuSelection){
                    MenuSelection.DELETE_ACTIVE_GOALS -> R.string.active_goals_deleted_toast
                    MenuSelection.DELETE_COMPLETED_GOALS -> R.string.completed_goals_deleted_toast
                    MenuSelection.DELETE_ALL_GOALS -> R.string.all_goals_deleted_toast
                    else -> 0
                }

                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle(
                        getString(dialogTitle)
                    )
                    .setMessage(R.string.alert_dialog_message)
                    .setPositiveButton(R.string.alert_dialog_delete) { _, _ ->
                        viewModel.deleteGoalsPerSelection(menuSelection)
                        createToast(deletedGoalsToastText)
                    }
                    .setNegativeButton(R.string.alert_dialog_cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
       return dialog
    }


    companion object {
        const val MENU_SELECTION = "MENU_SELECTION"
        const val TAG = "GoalsDeletionDialog"
    }

    fun setupGoalDeleteDialog(
        deletedGoalMenuSelection: MenuSelection
    ): GoalsDeletionDialogFragment {
        menuSelection = deletedGoalMenuSelection
        return this

    }
}
