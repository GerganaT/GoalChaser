package com.example.android.goalchaser.ui.activecompletedgoals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentActiveGoalsListBinding
import org.koin.android.ext.android.inject


class ActiveGoalsListFragment : Fragment() {

    lateinit var activeGoalsListBinding: FragmentActiveGoalsListBinding
    private val viewModel: ActiveCompletedGoalsViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activeGoalsListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_active_goals_list,
            container,
            false
        )
        activeGoalsListBinding.lifecycleOwner = viewLifecycleOwner

        activeGoalsListBinding.viewModel = viewModel
        return activeGoalsListBinding.root
    }


    //TODO show no list image when there're no tasks
    //TODO add transition to view/edit/create goal details
    //TODO add logic for fab button to reuse create_edit_goal fragment with label"create goal"


}