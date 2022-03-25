package com.example.android.goalchaser.ui.uistate

data class GoalDataUiState(
    var title: String,
    var dueDate: String,
    var sendNotification: Boolean = false,
    var timeUnitNumber: Int? = null,
    var days: Boolean = false,
    var months: Boolean = false,
    var isCompleted: Boolean = false,
    var id: Int = 0
)
