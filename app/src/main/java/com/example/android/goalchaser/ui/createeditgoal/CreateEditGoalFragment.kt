package com.example.android.goalchaser.ui.createeditgoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentCreateEditGoalBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreateEditGoalFragment : Fragment() {

    lateinit var createEditGoalBinding: FragmentCreateEditGoalBinding
    val viewModel: CreateEditGoalViewModel by viewModel()

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
        createEditGoalBinding.viewModel = viewModel
        createEditGoalBinding.lifecycleOwner = viewLifecycleOwner
        createNotificationSettingsDropdownMenu()
        return createEditGoalBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createEditGoalBinding.saveGoalFab.setOnClickListener {
            viewModel.saveGoal()
            viewModel.isTitleEntered.observe(viewLifecycleOwner){
                isTitleEntered -> if (!isTitleEntered){
                val snackbar =    Snackbar.make(createEditGoalBinding.root,R.string.no_title_entered_notification,
                    Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction(R.string.ok){snackbar.dismiss()}
                snackbar.show()
                }
            }
            viewModel.goalIsSaved.observe(viewLifecycleOwner) { isSaved ->
                if (isSaved && findNavController()
                        .currentDestination?.id == R.id.createEditGoalFragment
                ) {
                    findNavController().navigate(
                        CreateEditGoalFragmentDirections
                            .actionCreateEditGoalFragmentToActiveGoalsFragment()
                    )
                }
            }
        }
    }

    private fun createNotificationSettingsDropdownMenu() {
        val dayMonthsArray = requireActivity().resources.getStringArray(R.array.days_months)
        val dayMonthsArrayAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.days_months_dropdown_menu_item,
            dayMonthsArray
        )
        createEditGoalBinding.daysOrMonthsAutocompleteText.setAdapter(dayMonthsArrayAdapter)

        val dayMonthsNumberArray = arrayOf(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31
        )
        val dayMonthsNumberAdapter = ArrayAdapter(
            requireActivity(), R.layout.days_months_dropdown_menu_item,
            dayMonthsNumberArray
        )
        createEditGoalBinding.daysOrMonthsNumberAutocompleteText.setAdapter(dayMonthsNumberAdapter)
    }
}

//TODO when notifications are turned off /on show toast
//TODO optimize boilerplate above
//TODO Persist recycler view adapter item position throughout orientations
