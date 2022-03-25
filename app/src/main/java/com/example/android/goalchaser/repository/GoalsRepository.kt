package com.example.android.goalchaser.repository

import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.localdatasource.GoalsDao
import com.example.android.goalchaser.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoalsRepository(
    private val goalsDao: GoalsDao
) {
    suspend fun getGoals(): Result<List<GoalData>> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(goalsDao.getAllGoals())
            } catch (e: Exception) {
                Result.Error(e.localizedMessage)
            }
        }

    suspend fun deleteGoals() =
        withContext(Dispatchers.IO) {
            goalsDao.deleteAllGoals()
        }

    suspend fun saveGoal(goalData: GoalData): Result<GoalData> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.insertGoal(goalData)
                Result.Success(goalData)
            } catch (e: Exception) {
                Result.Error(e.localizedMessage)
            }
        }

    suspend fun deleteGoal(goalId: Int) =
        withContext(Dispatchers.IO) {
            goalsDao.deleteGoal(goalId)
        }
}
