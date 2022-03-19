package com.example.android.goalchaser.repository

import com.example.android.goalchaser.localdatasource.ImageDataDao
import com.example.android.goalchaser.localdatasource.ImageLocalData
import com.example.android.goalchaser.remotedatasource.ImageDataApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ImageDataRepository(
    private val imageDataApiService: ImageDataApiService,
    private val imageDataDao: ImageDataDao
) {
    suspend fun saveImageData() {
        try {
            withContext(Dispatchers.IO) {
                val remoteImageData = imageDataApiService.getImageOfTheDayData()
                val imageLocalData =
                    remoteImageData.run {
                        ImageLocalData(
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

    suspend fun getImageData(): ImageLocalData? {
        var imageLocalData: ImageLocalData?
        try {
            withContext(Dispatchers.IO) {

                imageLocalData = imageDataDao.getLastSavedImageData()
            }
        } catch (exception: Exception) {
            Timber.i(exception.localizedMessage)
            imageLocalData = null
        }
        return imageLocalData

    }

}
