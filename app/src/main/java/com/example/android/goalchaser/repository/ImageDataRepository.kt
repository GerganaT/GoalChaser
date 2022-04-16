package com.example.android.goalchaser.repository

import com.example.android.goalchaser.localdatasource.ImageDataDao
import com.example.android.goalchaser.localdatasource.ImageData
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
