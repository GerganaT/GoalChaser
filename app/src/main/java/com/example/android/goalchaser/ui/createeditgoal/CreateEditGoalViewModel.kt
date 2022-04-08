package com.example.android.goalchaser.ui.createeditgoal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.repository.GoalsRepository
import com.example.android.goalchaser.ui.uistate.GoalDataUiState
import com.example.android.goalchaser.utils.Result
import kotlinx.coroutines.launch

class CreateEditGoalViewModel(
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    val goalTitle = MutableLiveData<String>()
    val goalDueDate = MutableLiveData<String>()
    val activeNotification = MutableLiveData<Boolean>() // false
    val timeUnitCount = MutableLiveData<Int>()
    val timeTypeDays = MutableLiveData<Boolean>() //false
    val timeTypeMonths = MutableLiveData<Boolean>() // false
    val isActive = MutableLiveData<Boolean>() // false

    val goalIsSaved: LiveData<Boolean>
        get() = _goalIsSaved
    private val _goalIsSaved = MutableLiveData<Boolean>()


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
            //     if (goalTitle.value.isNullOrEmpty() || goalDueDate.value.isNullOrEmpty()) {
            GoalDataUiState(
                goalTitle.value,
                goalDueDate.value,
                activeNotification.value,
                timeUnitCount.value,
                timeTypeDays.value,
                timeTypeMonths.value,
                isActive.value
            ).run { saveUiState(this) }
            //   }

        }
    }

    fun saveDays(days: String) {
        if (activeNotification.value == true) {   //TODO logic doesn't work as expected.saves only 1
            timeUnitCount.value = days.toInt()
        }
        timeUnitCount.value = days.toInt()
    }

    //TODO Save all the values - even the defaults
    //TODO fix the save logic the outcommented code

//TODO finish the viewmodel
}


