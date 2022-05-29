package com.example.android.goalchaser.localdatasource

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "goals")
data class GoalData(
    val title: String?,
    val dueDate: String?,
    val sendNotification: Boolean?,
    val timeUnitNumber: Int?,
    val days: Boolean?,
    val months: Boolean?,
    val isCompleted: Boolean?,
    val notificationId:Int?,
    @PrimaryKey(autoGenerate = true) var goalId: Int = 0
):Serializable


