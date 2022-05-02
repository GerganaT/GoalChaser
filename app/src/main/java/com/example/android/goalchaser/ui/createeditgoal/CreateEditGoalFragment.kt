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
import androidx.navigation.fragment.navArgs
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentCreateEditGoalBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*


class CreateEditGoalFragment : Fragment() {

    private lateinit var createEditGoalBinding: FragmentCreateEditGoalBinding
    private lateinit var goalDatePicker: DatePicker
    private lateinit var goalDueDaysOrMonths: AutoCompleteTextView
    private lateinit var goalDueDaysMonthsAmount: AutoCompleteTextView
    private val viewModel: CreateEditGoalViewModel by viewModel()

    val args: CreateEditGoalFragmentArgs by navArgs()

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
            outState.putInt(YEAR, year)
            outState.putInt(MONTH, month)
            outState.putInt(DAY, dayOfMonth)
        }
        outState.putString(DAYS_NUMBER, goalDueDaysMonthsAmount.text.toString())
        outState.putString(DAYS_MONTHS, goalDueDaysOrMonths.text.toString())

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            goalDatePicker.updateDate(
                getInt(YEAR),
                getInt(MONTH),
                getInt(DAY)
            )
            goalDueDaysMonthsAmount.setText(getString(DAYS_NUMBER), false)
            goalDueDaysOrMonths.setText(getString(DAYS_MONTHS), false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createEditGoalBinding.lifecycleOwner = viewLifecycleOwner
        createEditGoalBinding.viewModel = viewModel
        //prepopulate data in the fragment if the user is viewing details of an existing goal
        if (args.passedGoalId != 0) {
            viewModel.run {
                getGoal(args.passedGoalId)
                goal.observe(viewLifecycleOwner) { savedGoal ->
                    savedGoal.dueDate?.split("/")
                        ?.map { it.toInt() }?.run {
                            val year = get(2)
                            // month is - 1 to be properly displayed within the Ui dialogs
                            val uiAdjustedMonth = get(0) - 1
                            val month = get(0)
                            val day = get(1)
                            goalDatePicker.updateDate(
                                year, uiAdjustedMonth, day
                            )
                            val goalMinDueDate = LocalDate.now().plus(1, ChronoUnit.DAYS)
                            val goalDueDate = LocalDate.of(year, month, day)
                            if (goalMinDueDate.isAfter(goalDueDate)) {
                                val monthName = DateFormatSymbols(Locale.getDefault())
                                    .months[uiAdjustedMonth]

                                if (savedInstanceState == null) {
                                    Snackbar.make(
                                        createEditGoalBinding.root, getString(
                                            R.string.goal_adjusted_snackbar_message,
                                            day,
                                            monthName,
                                            year
                                        ),
                                        Snackbar.LENGTH_INDEFINITE
                                    ).run {
                                        setAction(R.string.ok) {
                                            dismiss()
                                        }
                                    }.show()
                                }


                            }


                        }
                }

            }
        }
        goalDatePicker = createEditGoalBinding.goalDatePicker
        goalDueDaysOrMonths = createEditGoalBinding.daysOrMonthsAutocompleteText
        goalDueDaysMonthsAmount =
            createEditGoalBinding.daysOrMonthsNumberAutocompleteText

        createEditGoalBinding.saveGoalFab.setOnClickListener {
            viewModel.activeNotification.observe(viewLifecycleOwner)
            { notificationIsActive: Boolean? ->
                if (notificationIsActive == true) {
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
                    Snackbar.make(
                        createEditGoalBinding.root, R.string.no_title_entered_notification,
                        Snackbar.LENGTH_INDEFINITE
                    ).run { setAction(R.string.ok) { dismiss() } }.show()
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


    companion object {
        const val YEAR = "YEAR"
        const val MONTH = "MONTH"
        const val DAY = "DAY"
        const val DAYS_NUMBER = "DAYS_NUMBER"
        const val DAYS_MONTHS = "DAYS_MONTHS"

    }
}
//TODO update all ui fields properly when querying an existing goal /now only title,datepicker is getting updated
//TODO persist date adjusted snackbar through orientations
//TODO dismiss snackbars on navigation
//TODO Persist data in date picker when rotated but in edit-goal mode ; create mode works fine
//TODO persist no goal title entered snackbar throughout orientations
//TODO Write logic to update entry via the save button -then goal updated data has to be displayed
// TODO in the list
