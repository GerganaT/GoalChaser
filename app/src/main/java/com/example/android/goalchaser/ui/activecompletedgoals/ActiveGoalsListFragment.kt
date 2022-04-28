package com.example.android.goalchaser.ui.activecompletedgoals

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.goalchaser.R
import com.example.android.goalchaser.databinding.FragmentActiveGoalsListBinding
import com.example.android.goalchaser.ui.activecompletedgoals.recyclerView.GoalsListAdapter
import com.example.android.goalchaser.utils.uiutils.setupAlertDialog
import org.koin.android.ext.android.inject


class ActiveGoalsListFragment : Fragment() {

    lateinit var activeGoalsListBinding: FragmentActiveGoalsListBinding
    val viewModel: ActiveCompletedGoalsViewModel by inject()
    private var goalId: Int = 0
    private var goalTitle: String? = ""
    private var goalDeletionDialogFragment: GoalDeletionDialogFragment? = null
    private var bundle: Bundle? = null


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
        setupAdapter()
        activeGoalsListBinding.addActiveGoalFab.setOnClickListener {
            findNavController().navigate(
                ActiveGoalsListFragmentDirections.actionActiveGoalsFragmentToCreateEditGoalFragment()
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("GOAL_ID", goalId)
        outState.putString("GOAL_TITLE", goalTitle)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        bundle = savedInstanceState

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_list_menu, menu)
    }

    private fun setupAdapter() {

        val goalsListAdapter = GoalsListAdapter { selectedGoal, adapterView ->
            goalId = selectedGoal.id
            goalTitle = selectedGoal.title

            val popupTheme = ContextThemeWrapper(context, R.style.PopupMenuItemStyle)
            val popupMenu = PopupMenu(popupTheme, adapterView)
            popupMenu.run {
                inflate(R.menu.popup_menu_active_goals)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.details_popup_item -> {
                            Toast.makeText(context, "details clicked", Toast.LENGTH_SHORT).show()
                        }
                        R.id.mark_completed_popup_item -> {
                            Toast.makeText(context, "mark completed clicked", Toast.LENGTH_SHORT)
                                .show()
                        }
                        R.id.delete_popup_item -> {
                            goalDeletionDialogFragment = if (bundle == null) {
                                setupAlertDialog(goalId, goalTitle)

                            } else {
                                bundle?.let {
                                    setupAlertDialog(
                                        it.getInt("GOAL_ID"), it.getString("GOAL_TITLE")
                                    )
                                }
                            }

                            goalDeletionDialogFragment?.show(
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
    //TODO persist alert dialog throughout orientations
    //TODO add the respective functions to the popup menu functions - delete done
    //TODO show no list image when there're no tasks
    //TODO add transition to view/edit/create goal details
    //TODO add logic for fab button to reuse create_edit_goal fragment with label"create goal"


}