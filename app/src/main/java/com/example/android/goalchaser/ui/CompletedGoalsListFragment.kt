package com.example.android.goalchaser.ui

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
        val viewModel:GoalChaserViewModel by viewModels()
        fragmentCompletedGoalsListBinding.goalChaserViewModel = viewModel

        return fragmentCompletedGoalsListBinding.root
    }
//TODO show error image when there's no image in the db and no internet
    //TODO show no list image when there're no tasks

}