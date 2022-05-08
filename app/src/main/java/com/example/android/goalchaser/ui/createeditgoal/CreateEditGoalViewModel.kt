package com.example.android.goalchaser.ui.createeditgoal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.repository.GoalsRepository
import com.example.android.goalchaser.ui.uistate.GoalDataUiState
import com.example.android.goalchaser.utils.datautils.Result
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateEditGoalViewModel(
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    val goalTitle = MutableLiveData<String?>()
    val goalDueDate = MutableLiveData<String?>()
    val activeNotification = MutableLiveData<Boolean?>()
    private val timeUnitCount = MutableLiveData<Int?>()
    private val timeTypeDays = MutableLiveData<Boolean?>()
    private val timeTypeMonths = MutableLiveData<Boolean?>()
    private val isDone = MutableLiveData<Boolean?>()
    val goal = MutableLiveData<GoalDataUiState>()
    val days: LiveData<Array<Int>>
        get() = _days
    private val _days = MutableLiveData<Array<Int>>()

    val goalIsSaved: LiveData<Boolean>
        get() = _goalIsSaved
    private val _goalIsSaved = MutableLiveData<Boolean>()

    val isTitleEntered: LiveData<Boolean>
        get() = _isTitleEntered
    private val _isTitleEntered = MutableLiveData<Boolean>()
    init {
        _days.value = Array(31) { it + 1 }
        isDone.value = false

    }


    fun getGoal(transferredGoalId: Int) {
        viewModelScope.launch {
            goalsRepository.getGoal(transferredGoalId).run {
                when (this) {
                    is Result.Success -> {
                        goal.value =
                            data.let { gd: GoalData ->
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
                        Timber.e("Cant load goal")
                    }
                }
            }
        }
    }
    fun updateNotificationDetails(goal:GoalDataUiState)=
        goal.run {
            goalTitle.value = title
            goalDueDate.value = dueDate
            activeNotification.value = sendNotification
            timeUnitCount.value = timeUnitNumber
            timeTypeDays.value = days
            timeTypeMonths.value = months
            isDone.value = isCompleted
        }

     fun clearDaysMonths(){
         timeUnitCount.value = null
         timeTypeDays.value = null
         timeTypeMonths.value = null
     }
     fun resetActiveNotification(){
         activeNotification.value = false
     }


    private fun saveUiState(goalDataUiState: GoalDataUiState) {
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
                _goalIsSaved.value = when (this) {
                    is Result.Success -> data
                    is Result.Error -> false
                }
            }
        }
    }

    fun saveGoal() {
        viewModelScope.launch {
            if (!goalTitle.value.isNullOrEmpty()) {
                _isTitleEntered.value = true
                GoalDataUiState(
                    goalTitle.value,
                    goalDueDate.value,
                    activeNotification.value,
                    timeUnitCount.value,
                    timeTypeDays.value,
                    timeTypeMonths.value,
                    isDone.value
                ).run { saveUiState(this) }
            } else {
                _isTitleEntered.value = false
            }

        }
    }

    fun saveDays(days: String) {
        timeUnitCount.value = days.toInt()
    }

    fun saveDaysOrMonths(daysOrMonths: String, daysMonths: Array<String>) {
        timeTypeDays.value = daysOrMonths == daysMonths[0]
        timeTypeMonths.value = daysOrMonths == daysMonths[1]
    }

}


