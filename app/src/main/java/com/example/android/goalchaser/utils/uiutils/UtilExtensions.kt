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

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.activecompletedgoals.GoalsListFragment
import com.example.android.goalchaser.ui.activecompletedgoals.GoalsListFragmentDirections
import com.example.android.goalchaser.ui.uistate.GoalDataUiState

fun AutoCompleteTextView.setupDaysMonthsCountAdapter(days: Array<Int>, savedState: Bundle?) {

    val adjustedArray = when (days.size) {
        1 -> Array(31) { it + 1 }
        else -> days
    }
    val initialDaysValue = when (days.size) {
        1 -> adjustedArray[days[0] - 1]
        else -> days[0]
    }
    val daysNumberAdapter = ArrayAdapter(
        context, R.layout.days_months_dropdown_menu_item,
        adjustedArray
    )
    setAdapter(daysNumberAdapter)
    if (savedState == null) {
        setText(initialDaysValue.toString(), false)
    }

}

fun AutoCompleteTextView.setupSavedDaysMonthsValues(daysOrMonths: Boolean?, savedState: Bundle?) {
    val arrayPosition = when (daysOrMonths) {
        null, true -> 0
        false -> 1
    }
    if (savedState == null) {
        setText(setupDaysMonthsAdapter()[arrayPosition], false)
    }
}

fun AutoCompleteTextView.setupDaysMonthsAdapter(): Array<String> {
    val dayMonthsArray = context.resources.getStringArray(R.array.days_months)
    val daysMonthsAdapter = ArrayAdapter(
        context,
        R.layout.days_months_dropdown_menu_item,
        dayMonthsArray
    )
    setAdapter(daysMonthsAdapter)

    return dayMonthsArray
}

fun GoalsListFragment.navigateToCreateEditGoalFragment(selectedGoalId: Int = 0) {
    val createEditGoalLabel = when (selectedGoalId) {
        0 -> getString(R.string.create_goal_fragment_label)
        else -> getString(R.string.edit_goal_fragment_label)
    }
    val navDirections = GoalsListFragmentDirections
        .actionGoalsFragmentToCreateEditGoalFragment(selectedGoalId, createEditGoalLabel)
    findNavController().run {
        navigate(
            navDirections
        )
    }
}

fun GoalsListFragment.navigateToCompletedGoalDetailsFragment(selectedGoal: GoalDataUiState) {
    viewModel.setCompletedGoalDate(selectedGoal.dueDate)
    viewModel.setCompletedGoalTitle(selectedGoal.title)

    val navDirections =
        GoalsListFragmentDirections.actionGoalsListFragmentToCompletedGoalDetailsFragment()
    findNavController().run {
        navigate(
            navDirections
        )
    }
}

fun createToast(message: Int, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun GoalsListFragment.deleteAllGoals() {
    GoalsDeletionDialogFragment()
        .show(
            childFragmentManager,
            GoalsDeletionDialogFragment.TAG
        )
}