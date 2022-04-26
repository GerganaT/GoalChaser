package com.example.android.goalchaser.utils.uiutils

import android.widget.Toast
import com.example.android.goalchaser.R
import com.example.android.goalchaser.ui.activecompletedgoals.ActiveGoalsListFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun ActiveGoalsListFragment.deleteSelectedGoal(goalId:Int, goalTitle:String?){
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.alert_dialog_title)
        .setMessage(R.string.alert_dialog_message)
        .setPositiveButton(R.string.alert_dialog_delete) { _, _ ->
            viewModel.deleteGoal(goalId)
            Toast.makeText(
                context,
                getString(R.string.deleted_goal_toast, goalTitle),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.refreshGoals()
        }
        .setNegativeButton(R.string.alert_dialog_cancel) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}