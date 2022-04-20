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
import org.koin.android.ext.android.inject


class ActiveGoalsListFragment : Fragment() {

    lateinit var activeGoalsListBinding: FragmentActiveGoalsListBinding
    private val viewModel: ActiveCompletedGoalsViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_list_menu, menu)
    }

    private fun setupAdapter() {
        val adapter = GoalsListAdapter { selectedGoal, adapterView ->
            val popupTheme = ContextThemeWrapper(context, R.style.PopupMenuItemStyle)
            val popupMenu = PopupMenu(popupTheme, adapterView)
            popupMenu.run {
                inflate(R.menu.popup_menu_active_goals)
                setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId){
                     R.id.details_popup_item ->{
                         Toast.makeText(context,"details clicked",Toast.LENGTH_SHORT).show()}
                        R.id.mark_completed_popup_item ->{
                            Toast.makeText(context,"mark completed clicked",Toast.LENGTH_SHORT).show()}
                        R.id.delete_popup_item ->{
                            Toast.makeText(context,"delete clicked",Toast.LENGTH_SHORT).show()}
                    }

                    true
                }
                show()
            }

        }

        activeGoalsListBinding.activeGoalsListRecycler.adapter = adapter
        activeGoalsListBinding.activeGoalsListRecycler.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )


    }


    override fun onResume() {
        super.onResume()
        viewModel.refreshGoals()
    }
    //TODO add the respective functions to the popup menu functions and see answr on udacity
    //TODO show no list image when there're no tasks
    //TODO add transition to view/edit/create goal details
    //TODO add logic for fab button to reuse create_edit_goal fragment with label"create goal"
    //TODO Persist recycler view adapter item position throughout orientations

}