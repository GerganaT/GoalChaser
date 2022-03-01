package com.example.android.goalchaser.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.ImageOfTheDayApiService
import kotlinx.coroutines.launch

class GoalChaserViewModel: ViewModel() {
    val pictureUrlString = MutableLiveData<String>()
    val photographerName = MutableLiveData<String>()


    init {
        qetMotivationalQuoteImage()
    }

    private fun qetMotivationalQuoteImage(){
        viewModelScope.launch {
            pictureUrlString.value = ImageOfTheDayApiService.retrofitService.getImageOfTheDay()?.urls?.small
            photographerName.value = ImageOfTheDayApiService.retrofitService.getImageOfTheDay()?.user?.name
        }
    }

}