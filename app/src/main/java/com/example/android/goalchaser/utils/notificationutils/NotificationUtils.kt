package com.example.android.goalchaser.utils.notificationutils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
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

fun setNotificationAlert(context: Context, oldNotificationId: Int?) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
    val notificationIntent: PendingIntent by lazy {
        val intent = Intent(context, NotificationAlarmBroadcastReceiver::class.java)
        intent.putExtra(EXTRA_NotificationId, oldNotificationId)

        PendingIntent.getBroadcast(
            context,
            oldNotificationId!!,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
    alarmManager?.set(
        AlarmManager.RTC_WAKEUP,
        SystemClock.elapsedRealtime() + 60 * 1000,
        notificationIntent
    )
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
            setNotificationAlert(requireContext(), viewModel.notificationId.value)
        }
    }
}

fun cancelNotificationAlert(context: Context, requestCode: Int?) {
    requestCode?.let {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context, NotificationAlarmBroadcastReceiver::class.java)
        val cancellingPendingIntent =
            PendingIntent.getService(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        cancellingPendingIntent?.let { cancellingIntent ->
            alarmManager?.cancel(cancellingIntent)
        }
    }

}


const val EXTRA_NotificationId = "EXTRA_NotificationId"