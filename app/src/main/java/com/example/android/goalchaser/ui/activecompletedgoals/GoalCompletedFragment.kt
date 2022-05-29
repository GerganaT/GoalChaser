package com.example.android.goalchaser.ui.activecompletedgoals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentGoalCompletedBinding
import com.example.android.goalchaser.utils.notificationutils.cancelNotificationAlert
import org.koin.android.ext.android.inject


class GoalCompletedFragment : Fragment() {

    lateinit var goalCompletedBinding: FragmentGoalCompletedBinding
    private var actionBar:ActionBar?=null
    val viewModel: ActiveCompletedGoalsViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        goalCompletedBinding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_goal_completed, container, false
            )
        return goalCompletedBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goalCompletedBinding.viewModel = viewModel
        goalCompletedBinding.lifecycleOwner = viewLifecycleOwner
        viewModel.setGoalCompletedAnimationDisplayed(savedInstanceState == null)
        actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        goalCompletedBinding.goalCompletedButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onResume() {
        super.onResume()
        actionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        actionBar?.show()
    }
}

