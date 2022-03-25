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

    val pictureUrlString: LiveData<String>
        get() = _pictureUrl
    private val _pictureUrl = MutableLiveData<String>()

    val photographerCredentials: LiveData<ImageDataUiState>
        get() = _photographerCredentials
    private val _photographerCredentials = MutableLiveData<ImageDataUiState>()


    val goals: LiveData<List<GoalDataUiState>>
        get() = _goals
    private val _goals = MutableLiveData<List<GoalDataUiState>>()

    val goalsAreDeleted: LiveData<Boolean>
        get() = _goalsAreDeleted
    private val _goalsAreDeleted = MutableLiveData<Boolean>()

    val goalIsDeleted: LiveData<Boolean>
        get() = _goalIsDeleted
    private val _goalIsDeleted = MutableLiveData<Boolean>()

    val goalIsSaved: LiveData<Boolean>
        get() = _goalIsSaved
    private val _goalIsSaved = MutableLiveData<Boolean>()


    init {

        getImageData()
    }


    private fun getImageData() {

        viewModelScope.launch {
            imageDataRepository.getImageData().run {
                when (this) {
                    is Result.Success -> {
                        data.run {
                            val imageUiState = ImageDataUiState(
                                imageLink, photographerName, photographerProfile
                            )
                            _pictureUrl.value =
                                imageUiState.imageLink
                            _photographerCredentials.value = imageUiState
                        }
                    }

                    is Result.Error -> Timber.e(message)
                }
            }


        }


    }

    fun getGoals() {
        viewModelScope.launch {
            goalsRepository.getGoals().run {
                when (this) {
                    is Result.Success -> {
                        _goals.value =
                            data.map { gd: GoalData ->
                                GoalDataUiState(
                                    gd.title,
                                    gd.dueDate,
                                    gd.sendNotification,
                                    gd.timeUnitNumber,
                                    gd.days,
                                    gd.months,
                                    gd.isCompleted,
                                    gd.goalId
                                )

                            }
                    }
                    is Result.Error -> {
                        Timber.e(message)
                    }
                }
            }


        }

    }

    fun deleteGoals() {
        viewModelScope.launch {
            goalsRepository.deleteGoals().run {
                _goalsAreDeleted.value = when (this) {
                    is Result.Success -> data
                    is Result.Error -> false
                }
            }
        }
    }

    fun saveGoal(goalDataUiState: GoalDataUiState) {
        viewModelScope.launch {
            val goalData = goalDataUiState.run {
                GoalData(
                    title,
                    dueDate,
                    sendNotification,
                    timeUnitNumber,
                    days,
                    months,
                    isCompleted
                )
            }
            goalsRepository.saveGoal(goalData).run {
                _goalIsDeleted.value = when (this) {
                    is Result.Success -> data
                    is Result.Error -> false
                }
            }
        }
    }


    fun deleteGoal(goalId: Int) {
        viewModelScope.launch {
            goalsRepository.deleteGoal(goalId).run {
                _goalIsDeleted.value = when (this) {
                    is Result.Success -> data
                    is Result.Error -> false
                }
            }
        }
    }


}
//TODO Construct the new goal object from user input
val goalTitle = MutableLiveData<String>()
val dueDate= MutableLiveData<String>()
//TODO Set formatted string from values for MM/DD/YYYY
var sendNotification: Boolean = false
var timeUnitNumber: Int? = null
var days: Boolean = false
var months: Boolean = false
var isCompleted: Boolean = false
//TODO Use coin for dep.injection and implement properly in active/completed goals fragments






