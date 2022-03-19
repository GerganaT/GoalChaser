package com.example.android.goalchaser.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.android.goalchaser.localdatasource.getGoalChaserDatabase
import com.example.android.goalchaser.remotedatasource.ImageOfTheDayDataApiService
import com.example.android.goalchaser.repository.ImageDataRepository
import com.example.android.goalchaser.ui.uistate.ImageDataUiState
import kotlinx.coroutines.launch
import timber.log.Timber

class GoalChaserViewModel(application: Application) : AndroidViewModel(application) {
    val pictureUrlString = MutableLiveData<String>()
    val photographerCredentials = MutableLiveData<ImageDataUiState>()
    val goalChaserDatabase = getGoalChaserDatabase(application)


    init {

        qetMotivationalQuoteImage()
    }

    private fun qetMotivationalQuoteImage() {

        viewModelScope.launch {


            try {
                val repository = ImageDataRepository(
                    ImageOfTheDayDataApiService.retrofitService,
                    goalChaserDatabase.imageDao
                )

                repository.saveImageData()
                val imageDataUiState = repository.getImageData()?.run {
                    ImageDataUiState(
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

    //TODO Use coin for dep.injection
    class GoalChaserViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GoalChaserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GoalChaserViewModel(application) as T
            }

            throw  IllegalArgumentException("Unable to construct ViewModel")
        }

    }


}



