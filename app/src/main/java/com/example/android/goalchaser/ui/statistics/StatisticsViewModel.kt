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
package com.example.android.goalchaser.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.repository.GoalsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt

class StatisticsViewModel(
    val goalsRepository: GoalsRepository
) : ViewModel() {


    val allGoals: LiveData<Int>
        get() = _allGoals
    private val _allGoals = MutableLiveData<Int>()

    val allActiveGoals: LiveData<Int>
        get() = _allActiveGoals
    private val _allActiveGoals = MutableLiveData<Int>()

    val allCompletedGoals: LiveData<Int>
        get() = _allCompletedGoals
    private val _allCompletedGoals = MutableLiveData<Int>()

    val activeGoalsRatio: LiveData<Float?>
        get() = _activeGoalsRatio
    private val _activeGoalsRatio = MutableLiveData<Float?>()
    val completedGoalsRatio: LiveData<Float?>
        get() = _completedGoalsRatio
    private val _completedGoalsRatio = MutableLiveData<Float?>()

    private lateinit var activeAndCompletedGoalCountsJob: Job


    suspend fun getAllGoals() {
        _allGoals.value = goalsRepository.getAllGoalsCount()
        Timber.i("all goals are ${_allGoals.value}")
    }

    suspend fun getAllActiveGoals() {
        _allActiveGoals.value = goalsRepository.getActiveGoalsCount()
        Timber.i("active goals are ${_allActiveGoals.value} ")
    }

    suspend fun getAllCompletedGoals() {
        _allCompletedGoals.value = goalsRepository.getCompletedGoalsCount()
        Timber.i("completed goals are ${_allCompletedGoals.value} ")
    }

    fun getActiveAndCompletedGoalCounts() {
        activeAndCompletedGoalCountsJob =
        viewModelScope.launch {
            getAllGoals()
            getAllActiveGoals()
            getAllCompletedGoals()
        }
    }

    fun getActiveInactiveGoalsRatio() = viewModelScope.launch {
        if(!::activeAndCompletedGoalCountsJob.isInitialized)
            return@launch
        activeAndCompletedGoalCountsJob.join()
            _activeGoalsRatio.value = try {
                String.format("%.2f", ((_allActiveGoals.value!!.toFloat() / _allGoals.value!!.toFloat()))).toFloat()
            } catch (e: Exception) {
                0.0f
            }
            _completedGoalsRatio.value = try {
                String.format("%.2f", ((
                        _allCompletedGoals.value !!.toFloat()/_allGoals.value!!.toFloat()))).toFloat()
            } catch (e: Exception) {
                0.0f
            }
    }
}



