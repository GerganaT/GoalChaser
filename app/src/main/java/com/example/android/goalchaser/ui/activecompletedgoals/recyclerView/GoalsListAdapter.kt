package com.example.android.goalchaser.ui.activecompletedgoals.recyclerView

import android.view.View
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.uistate.GoalDataUiState

//Use data binding to show the goal on the item
class GoalsListAdapter(callBack: (selectedGoal: GoalDataUiState, adapterView: View) -> Unit) :
    GoalsRecyclerViewAdapter<GoalDataUiState>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.goal_list_item
}