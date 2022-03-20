package com.example.android.goalchaser.repository

import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.localdatasource.GoalsDao

class GoalsRepository(
    private val goalsDao: GoalsDao
) {
    suspend fun getGoals():List<GoalData> = goalsDao.getAllGoals()

    suspend fun deleteGoals() = goalsDao.deleteAllGoals()

    suspend fun insertGoal(goalData: GoalData) = goalsDao.insertGoal(goalData)

    suspend fun deleteGoal(goalId: Int) = goalsDao.deleteGoal(goalId)
}

//TODO finish the repo