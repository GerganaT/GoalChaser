package com.example.android.goalchaser.localdatasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 14, entities = [ImageData::class, GoalData::class])
abstract class GoalChaserDatabase : RoomDatabase() {
    abstract val imageDao: ImageDataDao
    abstract val goalsDao: GoalsDao
}

@Volatile
private lateinit var INSTANCE: GoalChaserDatabase


fun getGoalChaserDatabase(context: Context): GoalChaserDatabase {

    if (!::INSTANCE.isInitialized) {
        synchronized(GoalChaserDatabase::class.java) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                GoalChaserDatabase::class.java, "goal-chaser-db"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }

    return INSTANCE
}
