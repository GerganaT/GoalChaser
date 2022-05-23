package com.example.android.goalchaser.localdatasource

import androidx.room.*

@Dao
interface GoalsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGoal(goal: GoalData)

    @Query("UPDATE goals SET isCompleted = 1 WHERE goalId = :goalId ")
    suspend fun markGoalCompleted(goalId: Int)

    @Query("DELETE FROM goals WHERE goalId = :goalId ")
    suspend fun deleteGoal(goalId: Int)

    @Query("DELETE FROM goals")
    suspend fun deleteAllGoals()

    @Query("DELETE FROM goals WHERE isCompleted = 0")
    suspend fun deleteAllActiveGoals()

    @Query("DELETE FROM goals WHERE isCompleted = 1")
    suspend fun deleteAllCompletedGoals()

    @Query("SELECT * FROM goals WHERE goalId = :goalId")
    suspend fun getGoal(goalId: Int): GoalData

    @Query("SELECT * FROM goals")
    suspend fun getAllGoals():List<GoalData>

    @Query("SELECT * FROM goals WHERE isCompleted = 0")
    suspend fun getActiveGoals(): List<GoalData>

    @Query("SELECT * FROM goals WHERE isCompleted = 1")
    suspend fun getCompletedGoals(): List<GoalData>


}