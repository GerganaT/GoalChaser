package com.example.android.goalchaser.repository

import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.localdatasource.GoalsDao
import com.example.android.goalchaser.utils.datautils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoalsRepository(
    private val goalsDao: GoalsDao
) {

    suspend fun createGoal(goalData: GoalData): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.insertGoal(goalData)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e.message)
            }
        }

    suspend fun updateGoal(isGoalAdjusted: Boolean, goalData: GoalData): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                if (isGoalAdjusted) {
                    goalsDao.updateGoal(goalData)
                    Result.Success(true)
                } else {
                    Result.Error("")
                }
            } catch (e: Exception) {
                Result.Error(e.message)
            }
        }

    suspend fun markGoalCompleted(goalId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.markGoalCompleted(goalId)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e.message)
            }
        }


    suspend fun getGoal(goalId: Int): Result<GoalData> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(goalsDao.getGoal(goalId))
            } catch (e: Exception) {
                Result.Error(e.message)
            }
        }

    suspend fun getActiveGoals(): Result<List<GoalData>> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(goalsDao.getActiveGoals())
            } catch (e: Exception) {
                Result.Error(e.message)
            }
        }

    suspend fun deleteGoal(goalId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.deleteGoal(goalId)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e.message)
            }

        }

    suspend fun deleteGoals(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.deleteAllGoals()
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e.message)
            }

        }


}
