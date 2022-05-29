package com.example.android.goalchaser.background

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.repository.GoalsRepository
import com.example.android.goalchaser.utils.datautils.Result
import com.example.android.goalchaser.utils.notificationutils.EXTRA_NotificationId
import com.example.android.goalchaser.utils.notificationutils.sendNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class NotificationAlarmBroadcastReceiverService : JobIntentService(), CoroutineScope {
    val TAG = NotificationAlarmBroadcastReceiverService::class.simpleName as String
    var coroutineJob: Job = Job()
    override fun onHandleWork(intent: Intent) {
    val notificationId = intent.extras?.getInt(EXTRA_NotificationId)
        registerNotification(notificationId)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    private fun registerNotification(notificationId:Int?) {
        val context = applicationContext
        val goalsRepository: GoalsRepository by inject()
        CoroutineScope(coroutineContext).launch {
            val result = goalsRepository.getGoalByNotificationId(notificationId)
            if (result is Result.Success<GoalData>) {
                sendNotification(context, result.data)
            }
        }
    }

    companion object {
        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                NotificationAlarmBroadcastReceiverService::class.java, JOB_ID,
                intent
            )
        }
    }

}

