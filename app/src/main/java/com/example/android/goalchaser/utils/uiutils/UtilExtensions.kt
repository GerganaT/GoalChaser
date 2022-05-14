package com.example.android.goalchaser.utils.uiutils

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.findNavController
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.activecompletedgoals.ActiveGoalsListFragment
import com.example.android.goalchaser.ui.activecompletedgoals.ActiveGoalsListFragmentDirections

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

fun ActiveGoalsListFragment.navigateToCreateEditGoalFragment(selectedGoalId: Int = 0) {
    val createEditGoalLabel = when (selectedGoalId) {
        0 -> getString(R.string.create_goal_fragment_label)
        else -> getString(R.string.edit_goal_fragment_label)
    }
    val navDirections = ActiveGoalsListFragmentDirections
        .actionActiveGoalsFragmentToCreateEditGoalFragment(selectedGoalId, createEditGoalLabel)
    findNavController().run {
        navigate(
            navDirections
        )
    }
}