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

import com.example.android.goalchaser.localdatasource.ImageData
import com.example.android.goalchaser.localdatasource.ImageDataDao
import com.example.android.goalchaser.remotedatasource.ImageDataApiService
import com.example.android.goalchaser.utils.datautils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ImageDataRepository(
    private val imageDataApiService: ImageDataApiService,
    private val imageDataDao: ImageDataDao
) {
    private suspend fun saveImageData() {
        try {
            withContext(Dispatchers.IO) {
                val remoteImageData = imageDataApiService.getImageOfTheDayData()
                val imageLocalData =
                    remoteImageData.run {
                        ImageData(
                            randomImage.imageLink,
                            photographer.name,
                            photographer.profileLinks.profileLink
                        )
                    }
                imageDataDao.saveLastLoadedImageData(imageLocalData)
            }
        } catch (exception: Exception) {
            Timber.i(exception.localizedMessage)
        }
    }

    suspend fun getImageData(): Result<ImageData> =

        try {
            withContext(Dispatchers.IO) {
                saveImageData()
                Result.Success(imageDataDao.getLastSavedImageData())
            }
        } catch (exception: Exception) {
            Timber.i(exception.localizedMessage)
            Result.Error(exception.localizedMessage)

        }


}
