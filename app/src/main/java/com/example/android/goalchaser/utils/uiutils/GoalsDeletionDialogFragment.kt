/* Copyright 2022,  Gergana Kirilova

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
                createToast(R.string.all_goals_deleted_toast, requireContext())
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
