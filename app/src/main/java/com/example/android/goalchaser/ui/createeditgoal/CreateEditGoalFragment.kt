package com.example.android.goalchaser.ui.createeditgoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentCreateEditGoalBinding
import com.example.android.goalchaser.utils.uiutils.SavingMotionLayout
import com.example.android.goalchaser.utils.uiutils.SavingSnackbar
import com.example.android.goalchaser.utils.uiutils.setupDaysMonthsCountAdapter
import com.example.android.goalchaser.utils.uiutils.setupSavedDaysMonthsValues
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
    private val args: CreateEditGoalFragmentArgs by navArgs()
    private lateinit var savingMotionLayout: SavingMotionLayout
    private var noTitleSnackbar: Snackbar? = null
    private var savingSnackbar: SavingSnackbar? = null


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
        savingMotionLayout = createEditGoalBinding.root as SavingMotionLayout
        return createEditGoalBinding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            goalDatePicker.run {
                putInt(YEAR, year)
                putInt(MONTH, month)
                putInt(DAY, dayOfMonth)
            }

            putString(DAYS_NUMBER, goalDueDaysMonthsAmount.text.toString())
            putString(DAYS_MONTHS, goalDueDaysOrMonths.text.toString())

            savingSnackbar = SavingSnackbar(noTitleSnackbar)
            putSerializable(SNACKBAR, savingSnackbar)
        }


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

            savingSnackbar = getSerializable(SNACKBAR) as SavingSnackbar?
            noTitleSnackbar = savingSnackbar?.savedSnackbar
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = args.passedLabel
        createEditGoalBinding.lifecycleOwner = viewLifecycleOwner
        createEditGoalBinding.viewModel = viewModel
        goalDatePicker = createEditGoalBinding.goalDatePicker
        goalDueDaysOrMonths = createEditGoalBinding.daysOrMonthsAutocompleteText
        goalDueDaysMonthsAmount =
            createEditGoalBinding.daysOrMonthsNumberAutocompleteText

        //prepopulate data in the fragment if the user is viewing details of an existing goal
        if (args.passedGoalId != 0 && savedInstanceState == null) {
            viewModel.run {
                getGoal(args.passedGoalId)
                goal.observe(viewLifecycleOwner) { savedGoal ->
                    updateNotificationDetails(savedGoal)
                    if (activeNotification.value == true) {
                        savingMotionLayout.transitionToEnd()
                    }
                    savedGoal.timeUnitNumber?.let { setupDays(it) }
                    goalDueDate.value?.split("/")
                        ?.map { it.toInt() }?.run {
                            val editYear = get(2)
                            // month is - 1 to be properly displayed within the Ui dialogs
                            val editUiAdjustedMonth = get(0) - 1
                            val actualMonth = get(0)
                            val editDay = get(1)
                            goalDatePicker.updateDate(
                                editYear, editUiAdjustedMonth, editDay
                            )
                            val goalMinDueDate = LocalDate.now().plus(1, ChronoUnit.DAYS)
                            val goalDueDate = LocalDate.of(editYear, actualMonth, editDay)
                            if (goalMinDueDate.isAfter(goalDueDate)) {
                                val monthName = DateFormatSymbols(Locale.getDefault())
                                    .months[editUiAdjustedMonth]
                                Snackbar.make(
                                    createEditGoalBinding.root, getString(
                                        R.string.goal_adjusted_snackbar_message,
                                        editDay,
                                        monthName,
                                        editYear
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
        viewModel.days.observe(viewLifecycleOwner) { daysAmount ->
            daysAmount?.let {
                goalDueDaysMonthsAmount.setupDaysMonthsCountAdapter(
                    daysAmount,
                    savedInstanceState
                )
            }
        }
        viewModel.daysMonthsMediatorLiveData.observe(viewLifecycleOwner) { daysOrMonths ->
            goalDueDaysOrMonths.setupSavedDaysMonthsValues(daysOrMonths, savedInstanceState)
        }

        createEditGoalBinding.saveGoalFab.setOnClickListener {
            if (viewModel.activeNotification.value == null) {
                viewModel.resetActiveNotification()
            }
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
                } else {
                    viewModel.clearDaysMonths()
                }

            }
            viewModel.saveOrUpdateGoal(args.passedGoalId)


            viewModel.goalIsSaved.observe(viewLifecycleOwner) { isSaved ->
                if (isSaved && findNavController()
                        .currentDestination?.id == R.id.createEditGoalFragment
                ) {
                    Toast.makeText(context, R.string.goal_saved_toast, Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }

            viewModel.goalIsUpdated.observe(viewLifecycleOwner) { isUpdated ->
                if (isUpdated && findNavController()
                        .currentDestination?.id == R.id.createEditGoalFragment
                ) {
                    Toast.makeText(context, R.string.goal_updated_toast, Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }

        }
        viewModel.isTitleEntered.observe(viewLifecycleOwner) { isTitleEntered ->
            if (!isTitleEntered) {
                noTitleSnackbar = Snackbar.make(
                    createEditGoalBinding.root, R.string.no_title_entered_notification,
                    Snackbar.LENGTH_INDEFINITE
                ).run { setAction(R.string.ok) {
                    viewModel.confirmWarningClicked()
                    dismiss() } }
                noTitleSnackbar?.show()
            }
        }


    }


    companion object {
        const val YEAR = "YEAR"
        const val MONTH = "MONTH"
        const val DAY = "DAY"
        const val DAYS_NUMBER = "DAYS_NUMBER"
        const val DAYS_MONTHS = "DAYS_MONTHS"
        const val SNACKBAR = "SNACKBAR"
    }
}
//TODO show custom titles of the updated or created goal
//TODO no update toast should show up when goal hasn't been updated
//TODO persist date adjusted snackbar through orientations
//TODO dismiss snackbars on navigation
// TODO try fixing desugaring error?/optional/