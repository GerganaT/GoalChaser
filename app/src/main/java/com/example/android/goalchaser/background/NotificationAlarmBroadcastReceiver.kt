package com.example.android.goalchaser.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationAlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationAlarmBroadcastReceiverService.enqueueWork(context,intent)
    }
}