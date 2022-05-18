package com.example.android.goalchaser.ui.activecompletedgoals

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentActiveGoalsListBinding
import com.example.android.goalchaser.ui.activecompletedgoals.recyclerView.GoalsListAdapter
import com.example.android.goalchaser.utils.uiutils.navigateToCreateEditGoalFragment
import org.koin.android.ext.android.inject


class ActiveGoalsListFragment : Fragment() {

    lateinit var activeGoalsListBinding: FragmentActiveGoalsListBinding
    val viewModel: ActiveGoalsViewModel by inject()

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
        inflater.inflate(R.menu.tasks_list_menu, menu)
    }

    private fun setupRecyclerViewAdapter() {

        val goalsListAdapter = GoalsListAdapter { selectedGoal, adapterView ->


            val popupTheme = ContextThemeWrapper(context, R.style.PopupMenuItemStyle)
            val popupMenu = PopupMenu(popupTheme, adapterView)
            popupMenu.run {
                inflate(R.menu.popup_menu_active_goals)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.details_popup_item -> {
                            val passedId = selectedGoal.id
                            navigateToCreateEditGoalFragment(passedId)
                        }
                        R.id.mark_completed_popup_item -> {
                            Toast.makeText(context, "mark completed clicked", Toast.LENGTH_SHORT)
                                .show()
                        }
                        R.id.delete_popup_item -> {
                            GoalDeletionDialogFragment().also { dialogFragment ->
                                dialogFragment.goalId = selectedGoal.id
                                dialogFragment.goalTitle = selectedGoal.title

                            }.show(
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
    //TODO add the respective functions to the popup menu functions - delete done
    //TODO add logic to the delete all menu


}