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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastLoadedImageData(imageOfTheDayDataLocal: ImageData)

    @Query("SELECT * FROM image_data ORDER BY ImageDataId DESC LIMIT 1")
    suspend fun getLastSavedImageData(): ImageData

    @Query("DELETE FROM image_data WHERE imageLink = :imageLinkKey")
    suspend fun deleteImageData(imageLinkKey: String)


}

