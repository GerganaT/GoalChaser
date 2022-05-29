package com.example.android.goalchaser.ui.uistate

data class GoalDataUiState(
    val title: String?,
    val dueDate: String?,
    val sendNotification: Boolean?,
    val timeUnitNumber: Int?,
    val days: Boolean?,
    val months: Boolean?,
    val isCompleted: Boolean?,
    val notificationId: Int?,
    var id: Int = 0
)