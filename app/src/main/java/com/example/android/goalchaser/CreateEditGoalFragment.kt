package com.example.android.goalchaser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.goalchaser.databinding.FragmentCreateEditGoalBinding


class CreateEditGoalFragment : Fragment() {

    lateinit var createEditGoalBinding: FragmentCreateEditGoalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createEditGoalBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_create_edit_goal,
                container,
                false
            )
        return createEditGoalBinding.root
    }


}
//TODO Add notification settings menu to this fragment