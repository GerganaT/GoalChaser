package com.example.android.goalchaser.localdatasource

import androidx.room.*

@Dao
interface GoalsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGoal(goal: GoalData)

    @Query("DELETE FROM goals WHERE goalId = :goalId ")
    suspend fun deleteGoal(goalId: Int)

    @Query("DELETE FROM goals")
    suspend fun deleteAllGoals()

    @Query("SELECT * FROM goals WHERE goalId = :goalId")
    suspend fun getGoal(goalId: Int): GoalData

    @Query("SELECT * FROM goals")
    suspend fun getAllGoals(): List<GoalData>


}