package com.tcorner.appbrella.util.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tcorner.appbrella.service.notification.NotificationService
import java.util.Calendar

class AlarmHelper(val context: Context) {

    companion object {
        const val NOTIFICATION_ACTION = "NOTIFICATION_ACTION"
    }

    private var manager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private var alarmIntent = Intent(context, NotificationService::class.java)
        .setAction("NOTIFICATION_ACTION")
        .let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

    fun start() {
        val calendar = Calendar.getInstance()
            .apply {
                set(Calendar.HOUR_OF_DAY, 10)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }

        manager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis + AlarmManager.INTERVAL_DAY,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

    fun cancel() {
        manager.cancel(alarmIntent)
    }
}