package com.example.android.goalchaser.utils.uiutils

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.activecompletedgoals.GoalsListFragment
import com.example.android.goalchaser.ui.activecompletedgoals.GoalsListFragmentDirections
import com.example.android.goalchaser.ui.uistate.GoalDataUiState
import timber.log.Timber

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

fun Fragment.createToast(message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

//TODO replace toast creation throughout the project with this helper-method

fun GoalsListFragment.deleteGoalsMenuSelection(menuSelection: MenuSelection) {
    viewModel.getAllGoals()
    viewModel.goalsEmptyCheckList.observe(viewLifecycleOwner){
        val isCompletedGoalsListEmpty = it?.filter { goalDataUiState ->
            goalDataUiState.isCompleted == true
        }.isNullOrEmpty()
        val isActiveGoalsListEmpty = it?.filter { goalDataUiState ->
            goalDataUiState.isCompleted == false
        }.isNullOrEmpty()
        val isAllGoalsListEmpty = it.isNullOrEmpty()
        if (isActiveGoalsListEmpty && menuSelection == MenuSelection.DELETE_ACTIVE_GOALS){
            createToast(R.string.nothing_to_delete_toast)
        }
        else if (isCompletedGoalsListEmpty && menuSelection == MenuSelection.DELETE_COMPLETED_GOALS){
            createToast(R.string.nothing_to_delete_toast)
        }
        else if (isAllGoalsListEmpty && menuSelection == MenuSelection.DELETE_ALL_GOALS )
            {
            createToast(R.string.nothing_to_delete_toast)
        } else {
            GoalsDeletionDialogFragment()
                .setupGoalDeleteDialog(menuSelection)
                .show(
                    childFragmentManager,
                    GoalsDeletionDialogFragment.TAG
                )
        }
    }

//TODO either fix the completed and active deletion to work or reduce it only to delete all
}