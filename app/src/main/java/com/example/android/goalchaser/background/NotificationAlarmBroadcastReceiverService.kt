package com.example.android.goalchaser.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.JobIntentService
import com.example.android.goalchaser.utils.notificationutils.sendNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class NotificationAlarmBroadcastReceiverService : JobIntentService(),CoroutineScope {
    val TAG = NotificationAlarmBroadcastReceiverService::class.simpleName as String
    var coroutineJob: Job = Job()
    override fun onHandleWork(intent: Intent) {
        registerNotification()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    private fun registerNotification(){
        val context = applicationContext
      //  val requestCode=0
//        val alarmPendingIntent:PendingIntent by lazy {
//            val alarmIntent = Intent(context, NotificationAlarmBroadcastReceiver::class.java)
//            PendingIntent.getBroadcast(
//                context,
//                requestCode,
//                alarmIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//        }
     //   val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
//        alarmManager?.set(
//            AlarmManager.RTC_WAKEUP,
//            SystemClock.elapsedRealtime() + 60 * 1000,
//            alarmPendingIntent
//        )
       sendNotification(context)
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