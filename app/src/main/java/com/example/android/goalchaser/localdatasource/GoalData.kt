package com.example.android.goalchaser.localdatasource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalData(
    val title: String,
    val dueDate: String,
    val sendNotification: Boolean = false,
    val timeUnitNumber: Int?,
    val days: Boolean = false,
    val months: Boolean = false,
    @PrimaryKey(autoGenerate = true) var goalId: Int=0
)


