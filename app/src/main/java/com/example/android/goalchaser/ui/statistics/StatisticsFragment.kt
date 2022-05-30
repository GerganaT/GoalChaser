/* Copyright 2022,  Gergana Kirilova

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.example.android.goalchaser.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.android.goalchaser.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatisticsFragment : Fragment() {
    lateinit var pieChart: PieChart
    private val viewModel: StatisticsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getActiveAndCompletedGoalCounts()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getActiveInactiveGoalsRatio().join()
            viewModel.allGoals.observe(viewLifecycleOwner) {
                if (it > 0) {
                    pieChart = activity?.findViewById(R.id.statistics_pie_chart) as PieChart
                    setupPieChart()
                    loadPieChartData()
                }
            }
        }
    }

    private fun setupPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.setDrawEntryLabels(false)
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = false
        val legend = pieChart.legend
        legend.typeface = context?.let { ResourcesCompat.getFont(it, R.font.actor) }
        legend.textSize = 20f
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.isEnabled = true

    }


    private fun loadPieChartData() {
        val tasksPercentage = arrayListOf(
            PieEntry(viewModel.activeGoalsRatio.value ?: 0.0f, "Active"),
            PieEntry(viewModel.completedGoalsRatio.value ?: 0.0f, "Completed")

        )
        val pieColors = arrayListOf<Int>()
        pieColors.add(ContextCompat.getColor(requireActivity(), R.color.colorAccent))
        pieColors.add(ContextCompat.getColor(requireActivity(), R.color.primaryColor))


        val pieDataSet = PieDataSet(tasksPercentage, "")
        pieDataSet.colors = pieColors
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)
        pieData.setValueFormatter(PercentFormatter(pieChart))
        pieData.setValueTextSize(20f)
        pieData.setValueTextColor(Color.BLACK)
        pieData.setValueTypeface(context?.let { ResourcesCompat.getFont(it, R.font.actor) })

        pieChart.data = pieData
        pieChart.invalidate()
        pieChart.animateY(2000, Easing.EaseInOutCirc);
    }

}
