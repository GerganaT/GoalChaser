package com.example.android.goalchaser.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.ImageOfTheDayApiService
import kotlinx.coroutines.launch
import java.lang.Exception

class GoalChaserViewModel: ViewModel() {
    val pictureUrlString = MutableLiveData<String>()
    val photographerName = MutableLiveData<String>()


    init {
        qetMotivationalQuoteImage()
    }

    private fun qetMotivationalQuoteImage(){

            viewModelScope.launch {
                try {
                    pictureUrlString.value = ImageOfTheDayApiService.retrofitService.getImageOfTheDay()?.urls?.small
                    photographerName.value = ImageOfTheDayApiService.retrofitService.getImageOfTheDay()?.user?.name
                }
                catch (e:Exception){
                    Log.i("viewmodel","Cannot connnect to the web")
                }

            }



    }

}