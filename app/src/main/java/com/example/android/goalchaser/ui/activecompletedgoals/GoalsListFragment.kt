package com.example.android.goalchaser.ui.activecompletedgoals

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentGoalsListBinding
import com.example.android.goalchaser.ui.activecompletedgoals.recyclerView.GoalsListAdapter
import com.example.android.goalchaser.utils.uiutils.*
import org.koin.android.ext.android.inject


class GoalsListFragment : Fragment() {

    private lateinit var activeGoalsListBinding: FragmentGoalsListBinding
    val viewModel: ActiveCompletedGoalsViewModel by inject()
    var popupMenu: PopupMenu? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activeGoalsListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_goals_list,
            container,
            false
        )

        return activeGoalsListBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activeGoalsListBinding.lifecycleOwner = viewLifecycleOwner
        activeGoalsListBinding.viewModel = viewModel
        setHasOptionsMenu(true)
        setupRecyclerViewAdapter()
        activeGoalsListBinding.addActiveGoalFab.setOnClickListener {
            navigateToCreateEditGoalFragment()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.goals_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_active_menu_item -> {
                viewModel.refreshGoals()
                createToast(R.string.active_goals_toast)
                viewModel.setActiveCompletedGoalSelection(MenuSelection.ACTIVE_GOALS)
                return true
            }
            R.id.show_completed_menu_item -> {
                viewModel.refreshGoals(MenuSelection.COMPLETED_GOALS)
                createToast(R.string.completed_goals_toast)
                viewModel.setActiveCompletedGoalSelection(MenuSelection.COMPLETED_GOALS)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerViewAdapter() {

        val goalsListAdapter = GoalsListAdapter { selectedGoal, adapterView ->

            val popupTheme = ContextThemeWrapper(context, R.style.PopupMenuItemStyle)
            popupMenu = PopupMenu(popupTheme, adapterView)

            popupMenu?.run {
                inflate(R.menu.popup_menu_active_goals)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.details_popup_item -> {
                            if (selectedGoal.isCompleted == false) {
                                val passedId = selectedGoal.id
                                navigateToCreateEditGoalFragment(passedId)
                            } else {
                                navigateToCompletedGoalDetailsFragment(selectedGoal)
                            }
                        }
                        R.id.mark_completed_popup_item -> {
                            GoalCompletionDialogFragment().setupGoalCompletionDialog(
                                selectedGoal.id,
                                selectedGoal.title
                            )
                                .show(
                                    childFragmentManager,
                                    GoalCompletionDialogFragment.TAG
                                )
                        }
                        R.id.delete_popup_item -> {
                            GoalDeletionDialogFragment().setupGoalDeleteDialog(
                                selectedGoal.id,
                                selectedGoal.title,
                                viewModel.activeCompletedGoalSelection.value
                            ).show(
                                childFragmentManager,
                                GoalDeletionDialogFragment.TAG
                            )
                        }
                    }
                    true
                }
                if (selectedGoal.isCompleted == true) {
                    popupMenu?.menu?.removeItem(R.id.mark_completed_popup_item)
                }
                show()

            }
        }


        activeGoalsListBinding.activeGoalsListRecycler.run {
            adapter = goalsListAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshGoals(
            viewModel.activeCompletedGoalSelection.value
                ?: MenuSelection.ACTIVE_GOALS
        )
    }


    //TODO add an enum or constants with menu options to trigger the different database operations so code in
    //TODO view model can be reused - done for active/completed goals
    //TODO add logic to the menu - done for active/completed goals
    //TODO show adapted no completed goals picture for completed goals empty state
    //TODO completion date != goal due date
    //TODO add custom selection color for overflow menu /optional if time/
}