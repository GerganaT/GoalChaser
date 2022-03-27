package com.example.android.goalchaser.ui.activecompletedgoals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentCompletedGoalsListBinding
import org.koin.android.ext.android.inject


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
        val viewModel: ActiveCompletedGoalsViewModel by inject()
        fragmentCompletedGoalsListBinding.lifecycleOwner = viewLifecycleOwner
        fragmentCompletedGoalsListBinding.viewModel = viewModel

        return fragmentCompletedGoalsListBinding.root
    }
    //TODO show no list image when there're no tasks

}