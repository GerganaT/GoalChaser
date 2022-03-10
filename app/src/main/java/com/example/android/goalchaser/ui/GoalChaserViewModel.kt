package com.example.android.goalchaser.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.remotedatasource.ImageOfTheDayApiService
import com.example.android.goalchaser.remotedatasource.User
import kotlinx.coroutines.launch
import java.lang.Exception

class GoalChaserViewModel: ViewModel() {
    val pictureUrlString = MutableLiveData<String>()
    val photographerCredentials = MutableLiveData<User>()


    init {
        qetMotivationalQuoteImage()
    }

    private fun qetMotivationalQuoteImage(){

            viewModelScope.launch {
                try {
                    pictureUrlString.value = ImageOfTheDayApiService.retrofitService.getImageOfTheDay()?.urls?.small
                    photographerCredentials.value = ImageOfTheDayApiService.retrofitService.getImageOfTheDay()?.user
                }
                catch (e:Exception){
                    Log.i("viewmodel","Cannot connnect to the web")
                }

            }



    }

}