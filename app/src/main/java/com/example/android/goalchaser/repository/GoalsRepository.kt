/* Copyright 2022,  Gergana Kirilova

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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

    suspend fun markGoalCompleted(goalId: Int, goalCompletionDate: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.markGoalCompleted(goalId, goalCompletionDate)
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

    suspend fun getAllGoals(): Result<List<GoalData>> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(goalsDao.getAllGoals())
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

    suspend fun getCompletedGoals(): Result<List<GoalData>> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(goalsDao.getCompletedGoals())
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

    suspend fun deleteAllGoals(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.deleteAllGoals()
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e.message)
            }

        }

    suspend fun getGoalByNotificationId(notificationId: Int?): Result<GoalData> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(goalsDao.getGoalByNotificationId(notificationId))
            } catch (e: Exception) {
                Result.Error(e.message)
            }

        }

    suspend fun clearNotificationDataOnGoalCompletion(goalId: Int): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                goalsDao.clearNotificationDataOnGoalCompletion(goalId)
                Result.Success(true)
            } catch (e: Exception) {
                Result.Error(e.message)
            }
        }

    suspend fun getActiveGoalsCount(): Int =
        withContext(Dispatchers.IO) {
            return@withContext try {
                goalsDao.getActiveGoalsCount()
            } catch (e: Exception) {
                0
            }
        }

    suspend fun getCompletedGoalsCount(): Int =
        withContext(Dispatchers.IO) {
            return@withContext try {
                goalsDao.getCompletedGoalsCount()
            } catch (e: Exception) {
                0
            }
        }

    suspend fun getAllGoalsCount(): Int =
        withContext(Dispatchers.IO) {
            return@withContext try {
                goalsDao.getAllGoalsCount()
            } catch (e: Exception) {
                0
            }
        }
}


