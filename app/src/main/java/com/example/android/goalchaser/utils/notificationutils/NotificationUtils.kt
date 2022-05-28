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
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.example.android.goalchaser.BuildConfig
import com.example.android.goalchaser.R
import com.example.android.goalchaser.background.NotificationAlarmBroadcastReceiver
import com.example.android.goalchaser.localdatasource.GoalData
import com.example.android.goalchaser.ui.MainActivity
import com.example.android.goalchaser.ui.createeditgoal.CreateEditGoalFragment
import com.example.android.goalchaser.ui.createeditgoal.CreateEditGoalViewModel

private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"


fun sendNotification(context: Context, goalData: GoalData? = null) {


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

//    val intent = ReminderDescriptionActivity.newIntent(context.applicationContext, goalData)
//
//    //create a pending intent that opens ReminderDescriptionActivity when the user clicks on the notification
//    val stackBuilder = TaskStackBuilder.create(context)
//        .addParentStack(ReminderDescriptionActivity::class.java)
//        .addNextIntent(intent)
//    val notificationPendingIntent = stackBuilder
//        .getPendingIntent(getUniqueId(), PendingIntent.FLAG_UPDATE_CURRENT)
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

//    build the notification object with the data to be shown
    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle("some title")
        .setContentText("notification popped")
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager?.notify(getUniqueId(), notification)
}

private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())

// this check is needed for API 31 + as it requires permission to set alarms at exact time
//taken from this article:
//https://betterprogramming.pub/scheduled-notifications-in-android-2055356fb4f5
fun checkAlarmsAccess(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        return true
    }
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
    return alarmManager?.canScheduleExactAlarms() == true
}

fun Fragment.setNotificationAlert(context: Context) {

    val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
    val notificationIntent:PendingIntent by lazy {
        val intent = Intent(context, NotificationAlarmBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_IMMUTABLE)
    }

    if (checkAlarmsAccess(context) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        alarmManager?.setExact(
            AlarmManager.RTC_WAKEUP,
            SystemClock.elapsedRealtime() + 30 * 1000,
            notificationIntent
        )
    } else {
        alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            SystemClock.elapsedRealtime() + 30 * 1000,
            notificationIntent
        )
    }


}

fun Fragment.checkIfUpdatedAndSendNotification(viewModel:CreateEditGoalViewModel){
    when{
        viewModel.goalIsCreated.value == true ||
        viewModel.goal.value?.dueDate != viewModel.goalDueDate.value ||
        viewModel.goal.value?.sendNotification==false||
        viewModel.goal.value?.timeUnitNumber != viewModel.timeUnitCount.value||
        viewModel.goal.value?.days != viewModel.timeTypeDays.value ||
        viewModel.goal.value?.months != viewModel.timeTypeMonths.value
        -> setNotificationAlert(requireContext())
    }
}

