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
package com.example.android.goalchaser.localdatasource

import androidx.room.*

@Dao
interface GoalsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGoal(goal: GoalData)

    @Query("UPDATE goals SET isCompleted = 1 ,dueDate = :goalCompletionDate WHERE goalId = :goalId")
    suspend fun markGoalCompleted(goalId: Int, goalCompletionDate: String)

    @Query(
        "UPDATE goals SET timeUnitNumber = null," +
                " days = null,months = null, notificationId = null WHERE goalId = :goalId"
    )
    suspend fun clearNotificationDataOnGoalCompletion(goalId: Int)

    @Query("DELETE FROM goals WHERE goalId = :goalId ")
    suspend fun deleteGoal(goalId: Int)

    @Query("DELETE FROM goals")
    suspend fun deleteAllGoals()

    @Query("SELECT * FROM goals WHERE goalId = :goalId")
    suspend fun getGoal(goalId: Int): GoalData

    @Query("SELECT * FROM goals")
    suspend fun getAllGoals(): List<GoalData>

    @Query("SELECT * FROM goals WHERE isCompleted = 0")
    suspend fun getActiveGoals(): List<GoalData>

    @Query("SELECT * FROM goals WHERE isCompleted = 1")
    suspend fun getCompletedGoals(): List<GoalData>

    @Query("SELECT * FROM goals WHERE notificationId = :notificationId ")
    suspend fun getGoalByNotificationId(notificationId: Int?): GoalData

    @Query("SELECT COUNT (*) FROM goals WHERE isCompleted = 0")
    suspend fun getActiveGoalsCount(): Int

    @Query("SELECT COUNT (*) FROM goals WHERE isCompleted = 1")
    suspend fun getCompletedGoalsCount(): Int

    @Query("SELECT COUNT (*) FROM goals")
    suspend fun getAllGoalsCount(): Int

}