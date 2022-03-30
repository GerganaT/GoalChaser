package com.example.android.goalchaser.localdatasource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalData(
    var title: String?,
    var dueDate: String?,
    var sendNotification: Boolean? = false,
    var timeUnitNumber: Int? = null,
    var days: Boolean? = false,
    var months: Boolean? = false,
    var isCompleted: Boolean ?= false,
    @PrimaryKey(autoGenerate = true) var goalId: Int = 0
)


