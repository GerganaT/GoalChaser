package com.example.android.goalchaser.localdatasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastLoadedImageData(imageOfTheDayDataLocal: ImageLocalData)

    @Query("SELECT * FROM image_data ORDER BY ImageDataId DESC LIMIT 1")
    suspend fun getLastSavedImageData():ImageLocalData?


}

