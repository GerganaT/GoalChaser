package com.example.android.goalchaser.ui.activecompletedgoals

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentActiveGoalsListBinding
import com.example.android.goalchaser.ui.activecompletedgoals.recyclerView.GoalsListAdapter
import com.example.android.goalchaser.utils.uiutils.*
import org.koin.android.ext.android.inject


class GoalsListFragment : Fragment() {

    lateinit var activeGoalsListBinding: FragmentActiveGoalsListBinding
    val viewModel: ActiveCompletedGoalsViewModel by inject()
    var popupMenu: PopupMenu? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activeGoalsListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_active_goals_list,
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
                return true
            }
            R.id.show_completed_menu_item -> {
                viewModel.refreshGoals(MenuSelection.COMPLETED_GOALS)
                createToast(R.string.completed_goals_toast)
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
                            val passedId = selectedGoal.id
                            navigateToCreateEditGoalFragment(passedId)
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
                                selectedGoal.id, selectedGoal.title
                            ).show(
                                childFragmentManager,
                                GoalDeletionDialogFragment.TAG
                            )
                        }
                    }
                    true
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
        viewModel.refreshGoals()
    }


    //TODO add an enum or constants with menu options to trigger the different database operations so code in
    //TODO view model can be reused
    //TODO add logic to the menu
    //TODO persist goals active or completed throughout device orientation
    //TODO decide how to act with popup menu in active/deleted cases
    //TODO when operation on goal is performed user has to return to his current selection
    //TODO show loading indicator or goal lottie logo maybe?
    //TODO persist popup menu throughout orientations when opened-is it worth it?-if mentor
    //TODO doesn't reply implement normal menu on each list item in adapter



}