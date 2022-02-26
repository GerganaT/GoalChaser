package com.example.android.goalchaser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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