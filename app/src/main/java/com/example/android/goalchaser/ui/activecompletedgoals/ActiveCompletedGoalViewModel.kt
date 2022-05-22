package com.example.android.goalchaser.ui.activecompletedgoals

import androidx.lifecycle.*
import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.repository.GoalsRepository
import com.example.android.goalchaser.repository.ImageDataRepository
import com.example.android.goalchaser.ui.uistate.GoalDataUiState
import com.example.android.goalchaser.ui.uistate.ImageDataUiState
import com.example.android.goalchaser.utils.datautils.Result
import kotlinx.coroutines.launch
import timber.log.Timber

class ActiveCompletedGoalViewModel(
    private val imageDataRepository: ImageDataRepository,
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    val pictureUrlString: LiveData<String>
        get() = _pictureUrl
    private val _pictureUrl = MutableLiveData<String>()
    val completedGoalTitle: LiveData<String?>
        get() = _completedGoalTitle
    private val _completedGoalTitle = MutableLiveData<String?>()

    val photographerCredentials: LiveData<ImageDataUiState>
        get() = _photographerCredentials
    private val _photographerCredentials = MutableLiveData<ImageDataUiState>()


    val goals = MutableLiveData<List<GoalDataUiState>>()

    val goalsAreDeleted: LiveData<Boolean>
        get() = _goalsAreDeleted
    private val _goalsAreDeleted = MutableLiveData<Boolean>()

    val goalIsCompleted: LiveData<Boolean>
        get() = _goalIsCompleted
    private val _goalIsCompleted = MutableLiveData<Boolean>()

    val goalCompletedAnimationDisplayed: LiveData<Boolean>
        get() = _goalCompletedAnimationDisplayed
    private val _goalCompletedAnimationDisplayed = MutableLiveData<Boolean>()

    val goalsListIsEmpty = goals.map { goals.value.isNullOrEmpty() }

    init {
        getImageData()
        getActiveGoals()
    }
    fun setCompletedGoalTitle(completedGoalTitle:String?){
        _completedGoalTitle.value = completedGoalTitle
    }
    fun setGoalCompletedAnimationDisplayed(isAlreadyAnimated:Boolean){
        _goalCompletedAnimationDisplayed.value = isAlreadyAnimated
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

                    is Result.Error -> Timber.e("No image loaded")
                }
            }


        }


    }

    private fun getActiveGoals() {
        viewModelScope.launch {
            goalsRepository.getActiveGoals().run {
                when (this) {
                    is Result.Success -> {
                        goals.value =
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
                        Timber.e("Cant load goals")
                    }
                }
            }


        }

    }

    // resolve data binding library issue , which doesn't allow me to call getGoals directly
    //in the fragment
    fun refreshGoals() = getActiveGoals()

    fun deleteGoals() {
        viewModelScope.launch {
            goalsRepository.deleteGoals().run {
                _goalsAreDeleted.value = this is Result.Success
            }
        }
    }
    fun markGoalCompleted(goalId: Int){
        viewModelScope.launch {
            goalsRepository.markGoalCompleted(goalId).run {
                _goalIsCompleted.value = this is Result.Success
            }
        }
    }

    fun deleteGoal(goalId: Int) {
        viewModelScope.launch {
            goalsRepository.deleteGoal(goalId)
        }
    }


}







