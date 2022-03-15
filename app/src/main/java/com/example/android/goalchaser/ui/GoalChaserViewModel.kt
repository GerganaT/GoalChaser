package com.example.android.goalchaser.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.remotedatasource.ImageOfTheDayApiService
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
                    ImageOfTheDayApiService.retrofitService.getImageOfTheDay()
                        ?.let { it.randomImage.imageLink }
                photographerCredentials.value =
                    ImageOfTheDayApiService.retrofitService.getImageOfTheDay()?.photographer


            } catch (e: Exception) {
                Timber.e("Cannot connect to the web")
            }

        }


    }

}
