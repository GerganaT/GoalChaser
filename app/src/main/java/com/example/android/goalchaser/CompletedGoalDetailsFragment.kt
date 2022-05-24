package com.example.android.goalchaser

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
import com.example.android.goalchaser.databinding.FragmentCompletedGoalDetailsBinding
import com.example.android.goalchaser.ui.activecompletedgoals.ActiveCompletedGoalsViewModel
import com.example.android.goalchaser.utils.uiutils.MenuSelection
import org.koin.android.ext.android.inject


class CompletedGoalDetailsFragment : Fragment() {
    private lateinit var completedGoalDetailsBinding: FragmentCompletedGoalDetailsBinding
    private val viewModel:ActiveCompletedGoalsViewModel by inject()
    private var actionBar: ActionBar?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        completedGoalDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_completed_goal_details, container, false
        )
        return completedGoalDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        completedGoalDetailsBinding.lifecycleOwner = viewLifecycleOwner
        completedGoalDetailsBinding.viewModel = viewModel
        actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        completedGoalDetailsBinding.gompletedGoalDetailsBtn.setOnClickListener{
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