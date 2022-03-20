package com.example.android.goalchaser.ui.uistate

data class GoalDataUiState(
    val title: String,
    val dueDate: String,
    val sendNotification: Boolean = false,
    val timeUnitNumber: Int?,
    val days: Boolean = false,
    val months: Boolean = false,
)
