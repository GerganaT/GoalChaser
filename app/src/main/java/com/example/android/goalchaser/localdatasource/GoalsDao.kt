package com.example.android.goalchaser.localdatasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GoalsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalData)

    @Query("DELETE FROM goals WHERE goalId = :goalId ")
    suspend fun deleteGoal(goalId:Int)

    @Query("SELECT * FROM goals")
    suspend fun getAllGoals():List<GoalData>

    @Query("DELETE FROM goals")
    suspend fun deleteAllGoals()




}