package com.example.android.goalchaser.utils.uiutils

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.android.goalchaser.R

fun AutoCompleteTextView.setupDaysMonthsCountAdapter(days: Array<Int>,savedState: Bundle?) {

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
    if (savedState == null){
        setText(initialDaysValue.toString(), false)
    }

}