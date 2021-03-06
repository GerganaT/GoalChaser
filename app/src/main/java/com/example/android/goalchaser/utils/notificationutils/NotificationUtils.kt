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
package com.example.android.goalchaser.utils.notificationutils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.example.android.goalchaser.ActiveGoalDetailsActivity
import com.example.android.goalchaser.BuildConfig
import com.example.android.goalchaser.R
import com.example.android.goalchaser.background.NotificationAlarmBroadcastReceiver
import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.ui.createeditgoal.CreateEditGoalViewModel
import timber.log.Timber
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"


fun sendNotification(context: Context, goalItem: GoalData) {


    val notificationManager = context
        .getSystemService<NotificationManager>()

    // We need to create a NotificationChannel associated with our CHANNEL_ID before sending a notification.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && notificationManager?.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null
    ) {
        val name = context.getString(R.string.app_name)
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager?.createNotificationChannel(channel)
    }

    val intent = ActiveGoalDetailsActivity.newIntent(context.applicationContext, goalItem)

    //create a pending intent that opens ActiveGoalDetailsActivity when the user clicks on the notification
    val stackBuilder = TaskStackBuilder.create(context)
        .addParentStack(ActiveGoalDetailsActivity::class.java)
        .addNextIntent(intent)
    goalItem.notificationId?.let {
        stackBuilder
            .getPendingIntent(it, PendingIntent.FLAG_IMMUTABLE)
    }




    goalItem.notificationId?.let {
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            it, intent, PendingIntent.FLAG_IMMUTABLE
        )
        //    build the notification object with the data to be shown
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(goalItem.title)
            .setContentText(
                context.getString(R.string.active_goal_due_date_notification, goalItem.dueDate)
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager?.notify(it, notification)
    }


}

fun setNotificationAlert(
    context: Context,
    notificationId: Int?,
    notificationTriggerDayCount: Long
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
    val notificationIntent: PendingIntent by lazy {
        val intent = Intent(context, NotificationAlarmBroadcastReceiver::class.java)
        intent.putExtra(EXTRA_NotificationId, notificationId)

        PendingIntent.getBroadcast(
            context,
            notificationId!!,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
    Timber.i("notificationtriggerdaycount is $notificationTriggerDayCount")
    if (notificationTriggerDayCount >= 0) {
        alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis()
                    + notificationTriggerDayCount * 24 * 60 * 60 * 1000,
            notificationIntent
        )
    } else {
        return
    }

}


fun Fragment.checkIfUpdatedAndSendNotification(viewModel: CreateEditGoalViewModel) {
    when {
        viewModel.goal.value?.id == 0 ||
                viewModel.goal.value?.dueDate != viewModel.goalDueDate.value ||
                viewModel.goal.value?.sendNotification == false ||
                viewModel.goal.value?.timeUnitNumber != viewModel.timeUnitCount.value ||
                viewModel.goal.value?.days != viewModel.timeTypeDays.value ||
                viewModel.goal.value?.months != viewModel.timeTypeMonths.value
        -> {
            viewModel.setNotificationId()
            viewModel.run {
                val notificationTriggerDate = setNotificationTriggerDate(
                    timeUnitCount.value,
                    timeTypeDays.value,
                    goalDueDate.value
                )
                setNotificationAlert(
                    requireContext(),
                    notificationId.value,
                    notificationTriggerDate
                )
            }

        }
    }
}

fun setNotificationTriggerDate(
    daysMonthsNumber: Int?,
    days: Boolean?,
    userSetGoalDueDate: String?
): Long {
    var year = 0
    var month = 0
    var day = 0

    userSetGoalDueDate?.split("/")
        ?.map { it.toInt() }?.run {
            year = get(2)
            month = get(0)
            day = get(1)
        }
    val goalDueDate = LocalDate.of(year, month, day).toEpochDay()

    var goalNotificationDate: Long = 0
    daysMonthsNumber?.let { number ->
        goalNotificationDate = LocalDate.now().plus(
            number.toLong(), if (days == true) ChronoUnit.DAYS else ChronoUnit.MONTHS
        ).toEpochDay()
    }
    val difference = goalDueDate - goalNotificationDate
    Timber.i("difference is $difference")
    return if (difference >= 0) {
        difference
    } else {
        return -1
    }
}

const val EXTRA_NotificationId = "EXTRA_NotificationId"