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
package com.example.android.goalchaser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.android.goalchaser.databinding.ActivityActiveGoalDetailsBinding
import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.ui.MainActivity

class ActiveGoalDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActiveGoalDetailsBinding

    companion object {
        private const val EXTRA_GoalUiStateItem = "EXTRA_GoalUiStateItem"

        //        receive the goal object after the user clicks on the notification
        fun newIntent(context: Context, goalItem: GoalData): Intent {
            val intent = Intent(context, ActiveGoalDetailsActivity::class.java)
            intent.putExtra(EXTRA_GoalUiStateItem, goalItem)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_active_goal_details
        )
        binding.activeGoalDetailsBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        binding.goalData =
            intent.getSerializableExtra(EXTRA_GoalUiStateItem) as GoalData
    }
}