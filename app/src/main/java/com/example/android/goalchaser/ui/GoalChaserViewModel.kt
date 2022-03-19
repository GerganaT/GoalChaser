package com.example.android.goalchaser.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.localdatasource.getGoalChaserDatabase
import com.example.android.goalchaser.remotedatasource.ImageOfTheDayDataApiService
import com.example.android.goalchaser.repository.ImageDataRepository
import com.example.android.goalchaser.ui.uistate.ImageDataUiState
import kotlinx.coroutines.launch
import timber.log.Timber

class GoalChaserViewModel : ViewModel() {
    val pictureUrlString = MutableLiveData<String>()
    val photographerCredentials = MutableLiveData<ImageDataUiState>()


    init {

        qetMotivationalQuoteImage()
    }

    private fun qetMotivationalQuoteImage() {

        viewModelScope.launch {
            val repository = ImageDataRepository(
                ImageOfTheDayDataApiService.retrofitService,
                getGoalChaserDatabase(Application()).imageDao
            )

            try {

                repository.saveImageData()
                val imageDataUiState = repository.getImageData()?.run { ImageDataUiState(
                    imageLink, photographerName, photographerProfile
                )
                }
                pictureUrlString.value =
                    imageDataUiState?.imageLink?.let { it }
                photographerCredentials.value = imageDataUiState?.let { it }



            } catch (e: Exception) {
                Timber.e("Cannot connect to the web")
            }

        }


    }

}
//TODO rewrite the view model to use the repo