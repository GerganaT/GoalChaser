package com.example.android.goalchaser.ui.activecompletedgoals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.repository.GoalsRepository
import com.example.android.goalchaser.repository.ImageDataRepository
import com.example.android.goalchaser.ui.uistate.GoalDataUiState
import com.example.android.goalchaser.ui.uistate.ImageDataUiState
import com.example.android.goalchaser.utils.Result
import kotlinx.coroutines.launch
import timber.log.Timber

class ActiveCompletedGoalsViewModel(
    application: Application,
    private val imageDataRepository: ImageDataRepository,
    private val goalsRepository: GoalsRepository
) : AndroidViewModel(application) {

    val pictureUrlString : LiveData<String>
    get() = _pictureUrlString
    private val _pictureUrlString = MutableLiveData<String>()

    val photographerCredentials:LiveData<ImageDataUiState>
    get() = _photographerCredentials

    private val _photographerCredentials = MutableLiveData<ImageDataUiState>()



    val goals: LiveData<List<GoalDataUiState>>
        get() = _goals
    private val _goals = MutableLiveData<List<GoalDataUiState>>()


    init {

        qetMotivationalQuoteImage()
    }

    private fun qetMotivationalQuoteImage() {

        viewModelScope.launch {


            try {
                imageDataRepository.saveImageData()
                val imageDataUiState = imageDataRepository.getImageData()?.run {
                    ImageDataUiState(
                        imageLink, photographerName, photographerProfile
                    )
                }
                _pictureUrlString.value =
                    imageDataUiState?.imageLink?.let { it }
                _photographerCredentials.value = imageDataUiState?.let { it }


            } catch (e: Exception) {
                Timber.e("Cannot connect to the web")
            }

        }


    }

    fun getGoals() {
        viewModelScope.launch {
            val result = goalsRepository.getGoals()
            when (result) {
                is Result.Success -> {
                    _goals.value =
                        result.data.map { gd: GoalData ->
                            GoalDataUiState(
                                gd.title,
                                gd.dueDate,
                                gd.sendNotification,
                                gd.timeUnitNumber,
                                gd.days,
                                gd.months,
                                gd.isCompleted
                            )

                        }
                }
                is Result.Error -> {
                    Timber.e(result.message)
                }
            }

        }

    }
    //TODO Finish this viewmodel
    //TODO Use coin for dep.injection and implement properly in active/completed goals fragments


}



