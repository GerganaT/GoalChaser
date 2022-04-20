package com.example.android.goalchaser.ui.createeditgoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentCreateEditGoalBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreateEditGoalFragment : Fragment() {

    private lateinit var createEditGoalBinding: FragmentCreateEditGoalBinding
    private lateinit var goalDatePicker: DatePicker
    private lateinit var goalDueDaysOrMonths: AutoCompleteTextView
    private lateinit var goalDueDaysMonthsAmount: AutoCompleteTextView
    private val viewModel: CreateEditGoalViewModel by viewModel()

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        goalDatePicker.run {
            outState.putInt("YEAR", year)
            outState.putInt("MONTH", month)
            outState.putInt("DAY", dayOfMonth)
        }
        outState.putString("DAYS_NUMBER", goalDueDaysMonthsAmount.text.toString())
        outState.putString("DAYS_MONTHS", goalDueDaysOrMonths.text.toString())

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            goalDatePicker.updateDate(
                getInt("YEAR"),
                getInt("MONTH"),
                getInt("DAY")
            )
            goalDueDaysMonthsAmount.setText(getString("DAYS_NUMBER"), false)
            goalDueDaysOrMonths.setText(getString("DAYS_MONTHS"), false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createEditGoalBinding.lifecycleOwner = viewLifecycleOwner
        createEditGoalBinding.viewModel = viewModel
        goalDatePicker = createEditGoalBinding.goalDatePicker
        goalDueDaysOrMonths = createEditGoalBinding.daysOrMonthsAutocompleteText
        goalDueDaysMonthsAmount =
            createEditGoalBinding.daysOrMonthsNumberAutocompleteText

        createEditGoalBinding.saveGoalFab.setOnClickListener {
            viewModel.activeNotification.observe(viewLifecycleOwner)
            { notificationIsActive: Boolean ->
                if (notificationIsActive) {
                    viewModel.saveDays(goalDueDaysMonthsAmount.text.toString())

                    val goalDaysOrMonthsAmount =
                        context?.resources?.getStringArray(R.array.days_months) as Array<String>
                    viewModel.saveDaysOrMonths(
                        goalDueDaysOrMonths.text.toString(),
                        goalDaysOrMonthsAmount
                    )
                }

            }
            viewModel.saveGoal()
            viewModel.isTitleEntered.observe(viewLifecycleOwner) { isTitleEntered ->
                if (!isTitleEntered) {
                    val snackbar = Snackbar.make(
                        createEditGoalBinding.root, R.string.no_title_entered_notification,
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackbar.setAction(R.string.ok) { snackbar.dismiss() }
                    snackbar.show()
                }
            }


            viewModel.goalIsSaved.observe(viewLifecycleOwner) { isSaved ->
                if (isSaved && findNavController()
                        .currentDestination?.id == R.id.createEditGoalFragment
                ) {
                    Toast.makeText(context, R.string.goal_saved_toast, Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }
}


