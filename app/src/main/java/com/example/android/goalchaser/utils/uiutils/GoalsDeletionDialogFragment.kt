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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle(
                        getString(R.string.alert_dialog_delete_all_goals)
                    )
                    .setMessage(R.string.alert_dialog_message)
                    .setPositiveButton(R.string.alert_dialog_delete) { _, _ ->
                        viewModel.deleteAllGoals()
                        createToast(R.string.all_goals_deleted_toast)
                    }
                    .setNegativeButton(R.string.alert_dialog_cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
       return dialog
    }


    companion object {
        const val TAG = "GoalsDeletionDialog"
    }

}
