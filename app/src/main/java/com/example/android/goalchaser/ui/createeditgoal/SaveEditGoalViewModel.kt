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

class SaveEditGoalViewModel(
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    val goalIsSaved: LiveData<Boolean>
        get() = _goalIsSaved
    private val _goalIsSaved = MutableLiveData<Boolean>()

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
                _goalIsSaved.value = when (this) {
                    is Result.Success -> data
                    is Result.Error -> false
                }
            }
        }
    }
    //TODO Construct the new goal object from user input
    val goalTitle = MutableLiveData<String>()
    val dueDate = MutableLiveData<String>()

    //TODO Set formatted string from values for MM/DD/YYYY
    var sendNotification: Boolean = false
    var timeUnitNumber: Int? = null
    var days: Boolean = false
    var months: Boolean = false
    var isCompleted: Boolean = false
//TODO finish the viewmodel
}


