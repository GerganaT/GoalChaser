package com.example.android.goalchaser.utils.uiutils

import androidx.fragment.app.Fragment
import com.example.android.goalchaser.ui.activecompletedgoals.GoalDeletionDialogFragment

 fun Fragment.setupAlertDialog(goalId:Int, goalTitle:String?)=
    GoalDeletionDialogFragment().also {
        it.goalId = goalId
        it.goalTitle = goalTitle
    }

