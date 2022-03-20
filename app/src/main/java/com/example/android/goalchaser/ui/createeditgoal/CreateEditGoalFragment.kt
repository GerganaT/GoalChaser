package com.example.android.goalchaser.ui.createeditgoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.goalchaser.R
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
        createNotificationSettingsDropdownMenu()
        return createEditGoalBinding.root
    }

    private fun createNotificationSettingsDropdownMenu() {
        val dayMonthsArray = requireActivity().resources.getStringArray(R.array.days_months)
        val dayMonthsArrayAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.days_months_dropdown_menu_item,
            dayMonthsArray)
        createEditGoalBinding.daysOrMonthsAutocompleteText.setAdapter(dayMonthsArrayAdapter)

        val dayMonthsNumberArray = arrayOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,
        21,22,23,24,25,26,27,28,29,30,31)
        val dayMonthsNumberAdapter = ArrayAdapter(
            requireActivity(), R.layout.days_months_dropdown_menu_item,
            dayMonthsNumberArray)
        createEditGoalBinding.daysOrMonthsNumberAutocompleteText.setAdapter(dayMonthsNumberAdapter)
    }
}

//TODO when notifications are turned off /on show toast
//TODO optimize boilerplate above
//TODO there's currently issue with menu's data in landscape and sometimes portrait.Implement viewmodel
//TODO properly and see if it's resolved.