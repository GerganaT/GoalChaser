/* Copyright 2022,  Gergana Kirilova

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.example.android.goalchaser.ui.createeditgoal

import android.os.SystemClock
import androidx.lifecycle.*
import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.repository.GoalsRepository
import com.example.android.goalchaser.ui.uistate.GoalDataUiState
import com.example.android.goalchaser.utils.datautils.Result
import com.example.android.goalchaser.utils.notificationutils.setNotificationTriggerDate
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateEditGoalViewModel(
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    val goalTitle = MutableLiveData<String?>()
    val goalDueDate = MutableLiveData<String?>()
    val activeNotification = MutableLiveData<Boolean?>()
    val timeUnitCount = MutableLiveData<Int?>()
    val timeTypeDays = MutableLiveData<Boolean?>()
    val timeTypeMonths = MutableLiveData<Boolean?>()
    private val isDone = MutableLiveData<Boolean?>()
    val goal = MutableLiveData<GoalDataUiState>()
    val days: LiveData<Array<Int>>
        get() = _days
    private val _days = MutableLiveData<Array<Int>>()

    val goalIsCreated: LiveData<Boolean>
        get() = _goalIsCreated
    private val _goalIsCreated = MutableLiveData<Boolean>()

    val goalIsUpdated: LiveData<Boolean>
        get() = _goalIsUpdated
    private val _goalIsUpdated = MutableLiveData<Boolean>()

    val isTitleEntered: LiveData<Boolean>
        get() = _isTitleEntered
    private val _isTitleEntered = MutableLiveData<Boolean>()

    val isDateAdjusted: LiveData<Boolean>
        get() = _isDateAdjusted
    private val _isDateAdjusted = MutableLiveData<Boolean>()

    val daysOrMonths = MediatorLiveData<Boolean?>()

    val notificationId: LiveData<Int?>
        get() = _notificationId
    private val _notificationId = MutableLiveData<Int?>()

    val isValidNotificationPeriodEntered: LiveData<Boolean>
        get() = _isValidNotificationPeriodEntered
    private val _isValidNotificationPeriodEntered = MutableLiveData<Boolean>()

    init {
        isDone.value = false
        _days.value = Array(31) { it + 1 }
        setDaysOrMonths()
    }

    fun defineValidNotificationEntered() {
        _isValidNotificationPeriodEntered.value = setNotificationTriggerDate(
            timeUnitCount.value,
            timeTypeDays.value,
            goalDueDate.value
        ) >= 0
    }

    fun setNotificationId() {
        _notificationId.value = ((SystemClock.uptimeMillis() % 10000).toInt())
    }

    fun confirmDateAdjusted(dateAdjusted: Boolean) {
        _isDateAdjusted.value = dateAdjusted
    }

    fun confirmWarningClicked() {
        _isTitleEntered.value = true
    }

    private fun setDaysOrMonths() {
        // MediatorLiveData returns true if user selected days option in notifications options
        daysOrMonths.addSource(timeTypeDays) {
            it?.let {
                if (it) {
                    daysOrMonths.value = it
                }
            }
            if (it == null) {
                daysOrMonths.value = null
            }
        }
        // MediatorLiveData returns false if user selected months option in notifications options
        daysOrMonths.addSource(timeTypeMonths) {
            it?.let {
                if (it) {
                    daysOrMonths.value = false
                }
            }
            if (it == null) {
                daysOrMonths.value = null
            }
        }
    }


    fun setupDays(days: Int) {
        _days.value = Array(1) { days }

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
                                    gd.notificationId,
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


    fun updateNotificationDetails(goal: GoalDataUiState) =
        goal.run {
            goalTitle.value = title
            goalDueDate.value = dueDate
            activeNotification.value = sendNotification
            timeUnitCount.value = timeUnitNumber
            timeTypeDays.value = days
            timeTypeMonths.value = months
            isDone.value = isCompleted
            _notificationId.value = notificationId
        }

    fun clearDaysMonths() {
        timeUnitCount.value = null
        timeTypeDays.value = null
        timeTypeMonths.value = null
    }

    fun resetActiveNotification() {
        activeNotification.value = false
    }


    private fun saveOrUpdateUiState(goalDataUiState: GoalDataUiState) {
        viewModelScope.launch {
            val goalData = goalDataUiState.run {
                GoalData(
                    title,
                    dueDate,
                    sendNotification,
                    timeUnitNumber,
                    days,
                    months,
                    isCompleted,
                    notificationId,
                    id
                )
            }
            when (goalData.goalId) {
                0 -> {
                    goalsRepository.createGoal(goalData).run {
                        _goalIsCreated.value = this is Result.Success
                    }
                }
                else -> {
                    goalsRepository.updateGoal(goal.value != goalDataUiState, goalData)
                        .run {
                            _goalIsUpdated.value = this is Result.Success
                        }
                }
            }

        }
    }

    fun createOrUpdateGoal(goalId: Int) {
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
                    isDone.value,
                    notificationId.value ?: 0,
                    id = if (goalId != 0) goalId else 0
                ).run { saveOrUpdateUiState(this) }
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


