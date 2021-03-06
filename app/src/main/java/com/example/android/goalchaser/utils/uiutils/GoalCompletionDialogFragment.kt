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
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.activecompletedgoals.ActiveCompletedGoalsViewModel
import com.example.android.goalchaser.ui.activecompletedgoals.GoalsListFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import java.time.LocalDate

/**This class solves the issue where AlertDialog is dismissed on device rotation as it is
 *lifecycle-aware*/
class GoalCompletionDialogFragment : DialogFragment() {
    private val viewModel: ActiveCompletedGoalsViewModel by inject()
    private var goalId = 0
    private var goalTitle: String? = ""
    private var goalCompletionDate = ""
    private var goalNotificationId: Int? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putInt(GOAL_ID, goalId)
            putString(GOAL_TITLE, goalTitle)
            putString(GOAL_COMPLETION_DATE, goalCompletionDate)
            goalNotificationId?.let { putInt(GOAL_NOTIFICATION_ID, it) }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        savedInstanceState?.run {
            goalId = getInt(GOAL_ID)
            goalTitle = getString(GOAL_TITLE)
            goalCompletionDate = getString(GOAL_COMPLETION_DATE).toString()
            goalNotificationId = getInt(GOAL_NOTIFICATION_ID)
        }
        LocalDate.now().run {
            goalCompletionDate = requireContext().getString(
                R.string.user_entered_date, monthValue, dayOfMonth, year
            )
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
                viewModel.clearNotificationDataOnGoalCompletion(goalId)
                viewModel.markGoalCompleted(goalId, goalCompletionDate)
                viewModel.setCompletedGoalTitle(goalTitle)
                dialog.dismiss()

                findNavController().navigate(
                    GoalsListFragmentDirections
                        .actionGoalsFragmentToGoalCompletedFragment()
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
        const val GOAL_COMPLETION_DATE = "GOAL_COMPLETION_DATE"
        const val GOAL_NOTIFICATION_ID = "GOAL_NOTIFICATION_ID"
    }

    fun setupGoalCompletionDialog(
        completedGoalId: Int,
        completedGoalTitle: String?,
        completedGoalNotificationId: Int?,
    ): GoalCompletionDialogFragment {
        goalId = completedGoalId
        goalTitle = completedGoalTitle
        goalNotificationId = completedGoalNotificationId
        return this
    }
}
