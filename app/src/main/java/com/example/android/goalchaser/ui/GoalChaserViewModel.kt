package com.example.android.goalchaser.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.remotedatasource.ImageOfTheDayDataApiService
import com.example.android.goalchaser.remotedatasource.Photographer
import kotlinx.coroutines.launch
import timber.log.Timber

class GoalChaserViewModel : ViewModel() {
    val pictureUrlString = MutableLiveData<String>()
    val photographerCredentials = MutableLiveData<Photographer>()


    init {
        qetMotivationalQuoteImage()
    }

    private fun qetMotivationalQuoteImage() {

        viewModelScope.launch {
            try {
                pictureUrlString.value =
                    ImageOfTheDayDataApiService.retrofitService.getImageOfTheDay()
                        ?.let { it.randomImage.imageLink }
                photographerCredentials.value =
                    ImageOfTheDayDataApiService.retrofitService.getImageOfTheDay()?.photographer


            } catch (e: Exception) {
                Timber.e("Cannot connect to the web")
            }

        }


    }

}