package com.example.android.goalchaser

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.android.goalchaser.databinding.ActivityActiveGoalDetailsBinding
import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.ui.MainActivity
import com.example.android.goalchaser.ui.uistate.GoalDataUiState

class ActiveGoalDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityActiveGoalDetailsBinding

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
        binding.activeGoalDetailsBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        binding.goalData =
            intent.getSerializableExtra(EXTRA_GoalUiStateItem) as GoalData
    }
}