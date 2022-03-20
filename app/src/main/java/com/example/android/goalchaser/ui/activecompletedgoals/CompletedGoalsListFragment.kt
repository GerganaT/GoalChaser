package com.example.android.goalchaser.ui.activecompletedgoals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentCompletedGoalsListBinding


class CompletedGoalsListFragment : Fragment() {
    lateinit var fragmentCompletedGoalsListBinding: FragmentCompletedGoalsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentCompletedGoalsListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_completed_goals_list,
            container,
            false
        )
        fragmentCompletedGoalsListBinding.lifecycleOwner = viewLifecycleOwner
        val viewModel: ActiveCompletedGoalsViewModel by viewModels()
        fragmentCompletedGoalsListBinding.goalChaserViewModel = viewModel

        return fragmentCompletedGoalsListBinding.root
    }
    //TODO optimize viewmodels
    //TODO show no list image when there're no tasks

}