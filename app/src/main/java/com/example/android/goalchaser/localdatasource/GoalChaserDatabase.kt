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
