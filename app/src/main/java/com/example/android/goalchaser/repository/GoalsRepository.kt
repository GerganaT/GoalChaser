package com.example.android.goalchaser.repository

import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.localdatasource.GoalsDao
import com.example.android.goalchaser.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoalsRepository(
    private val goalsDao: GoalsDao
) {
    suspend fun getGoals() = withContext(Dispatchers.IO) {
        try {
            return@withContext Result.Success(goalsDao.getAllGoals())
        } catch (e: Exception) {
            return@withContext Result.Error(e.localizedMessage)
        }
    }

    suspend fun deleteGoals() = goalsDao.deleteAllGoals()

    suspend fun insertGoal(goalData: GoalData) = goalsDao.insertGoal(goalData)

    suspend fun deleteGoal(goalId: Int) = goalsDao.deleteGoal(goalId)
}

