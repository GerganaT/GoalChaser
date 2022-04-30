package com.example.android.goalchaser.repository

import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.localdatasource.GoalsDao
import com.example.android.goalchaser.utils.datautils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoalsRepository(
    private val goalsDao: GoalsDao
) {

    suspend fun saveGoal(goalData: GoalData): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.insertGoal(goalData)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e.localizedMessage)
            }
        }
    suspend fun updateGoal(goalData: GoalData): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.updateGoal(goalData)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e.localizedMessage)
            }
        }

    suspend fun getGoal(goalId: Int): Result<GoalData> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(goalsDao.getGoal(goalId))
            } catch (e: Exception) {
                Result.Error(e.localizedMessage)
            }
        }

    suspend fun getGoals(): Result<List<GoalData>> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(goalsDao.getAllGoals())
            } catch (e: Exception) {
                Result.Error(e.localizedMessage)
            }
        }

    suspend fun deleteGoal(goalId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.deleteGoal(goalId)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e.localizedMessage)
            }

        }

    suspend fun deleteGoals(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.deleteAllGoals()
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e.localizedMessage)
            }

        }




}
