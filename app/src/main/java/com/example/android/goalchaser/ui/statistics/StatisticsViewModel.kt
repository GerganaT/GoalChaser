package com.example.android.goalchaser.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.goalchaser.repository.GoalsRepository
import kotlinx.coroutines.launch
import timber.log.Timber

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
        viewModelScope.launch {
            getAllGoals()
            getAllActiveGoals()
            getAllCompletedGoals()
        }
    }

     fun getActiveInactiveGoalsRatio() {
        _activeGoalsRatio.value = try {
            String.format("%.2f", ((_allActiveGoals.value!! / _allGoals.value!!) )).toFloat()
        } catch (e: Exception) {
            0.0f
        }
        _completedGoalsRatio.value = try {
            String.format("%.2f", ((_allCompletedGoals.value!! / _allGoals.value!!) )).toFloat()
        } catch (e: Exception) {
            0.0f
        }

        Timber.i("  ratio active${_activeGoalsRatio.value}")
    }
}



