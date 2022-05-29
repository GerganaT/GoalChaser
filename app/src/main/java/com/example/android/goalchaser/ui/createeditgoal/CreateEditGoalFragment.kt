package com.example.android.goalchaser.ui.createeditgoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentCreateEditGoalBinding
import com.example.android.goalchaser.utils.notificationutils.checkIfUpdatedAndSendNotification
import com.example.android.goalchaser.utils.notificationutils.setNotificationAlert
import com.example.android.goalchaser.utils.uiutils.*
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
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
    private var noTitleSavingSnackbar: SavingSnackbar? = null
    private var dateAdjustedSnackbar: Snackbar? = null
    private var dateAdjustedSavingSnackbar: SavingSnackbar? = null
    private var adjustedDate: SavingAdjustedDate? = null


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

            noTitleSavingSnackbar = SavingSnackbar(noTitleSnackbar)
            putSerializable(NO_TITLE_SNACKBAR, noTitleSavingSnackbar)
            dateAdjustedSavingSnackbar = SavingSnackbar(dateAdjustedSnackbar)
            putSerializable(DATE_ADJUSTED_SNACKBAR, dateAdjustedSavingSnackbar)
            putSerializable(ADJUSTED_DATE, adjustedDate)
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

            noTitleSavingSnackbar = getSerializable(NO_TITLE_SNACKBAR) as SavingSnackbar?
            noTitleSnackbar = noTitleSavingSnackbar?.savedSnackbar
            dateAdjustedSavingSnackbar = getSerializable(DATE_ADJUSTED_SNACKBAR) as SavingSnackbar?
            dateAdjustedSnackbar = dateAdjustedSavingSnackbar?.savedSnackbar
            adjustedDate = getSerializable(ADJUSTED_DATE) as SavingAdjustedDate?
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

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    dateAdjustedSnackbar?.dismiss()
                    noTitleSnackbar?.dismiss()
                    noTitleSnackbar = null
                    noTitleSavingSnackbar = null
                    dateAdjustedSnackbar = null
                    dateAdjustedSavingSnackbar = null
                    adjustedDate = null
                    findNavController().navigateUp()
                }
            })
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
                                adjustedDate = SavingAdjustedDate(editDay, monthName, editYear)
                                viewModel.confirmDateAdjusted(true)

                            }


                        }
                }

            }
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

                    checkIfUpdatedAndSendNotification(viewModel)

                } else {
                    viewModel.clearDaysMonths()
                }

            }
            viewModel.createOrUpdateGoal(args.passedGoalId)


            viewModel.goalIsCreated.observe(viewLifecycleOwner) { isCreated ->
                if (isCreated && findNavController()
                        .currentDestination?.id == R.id.createEditGoalFragment
                ) {
                    Toast.makeText(
                        context,
                        getString(R.string.goal_created_toast, viewModel.goalTitle.value),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }
            }

            viewModel.goalIsUpdated.observe(viewLifecycleOwner) { isUpdated ->

                val updateMessage = when(isUpdated){
                    true -> getString(R.string.goal_updated_toast, viewModel.goalTitle.value)
                    else -> getString(R.string.nothing_to_update_toast )
                }
                if (findNavController()
                        .currentDestination?.id == R.id.createEditGoalFragment
                ) {
                    Toast.makeText(
                        context,
                        updateMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
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
        viewModel.daysOrMonths.observe(viewLifecycleOwner) { daysOrMonths ->
            goalDueDaysOrMonths.setupSavedDaysMonthsValues(daysOrMonths, savedInstanceState)
        }
        viewModel.isTitleEntered.observe(viewLifecycleOwner) { isTitleEntered ->
            if (!isTitleEntered) {
                noTitleSnackbar = Snackbar.make(
                    createEditGoalBinding.root, R.string.no_title_entered_notification,
                    Snackbar.LENGTH_INDEFINITE
                ).run {
                    setAction(R.string.ok) {
                        viewModel.confirmWarningClicked()
                        dismiss()
                    }
                }
                noTitleSnackbar?.show()
            }
        }
        viewModel.isDateAdjusted.observe(viewLifecycleOwner) { isAdjusted ->
            if (isAdjusted) {
                adjustedDate?.run {
                    dateAdjustedSnackbar = Snackbar.make(
                        createEditGoalBinding.root,
                        getString(
                            R.string.goal_adjusted_snackbar_message,
                            adjDate, adjName, adjYear
                        ),
                        Snackbar.LENGTH_INDEFINITE
                    ).run {
                        setAction(R.string.ok) {
                            viewModel.confirmDateAdjusted(false)
                            dismiss()
                        }
                    }
                    dateAdjustedSnackbar?.show()
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
        const val NO_TITLE_SNACKBAR = "SNACKBAR"
        const val DATE_ADJUSTED_SNACKBAR = "DATE_ADJUSTED_SNACKBAR"
        const val ADJUSTED_DATE = "ADJUSTED_DATE"
    }
}
//TODO overwrite notification if it has an existing id
//TODO cancel notification on goal deletion or on notification cancellation:see below
//TODO don't allow user to set invalid notification period

