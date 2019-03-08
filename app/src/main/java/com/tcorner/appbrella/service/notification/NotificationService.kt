package com.tcorner.appbrella.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.tcorner.appbrella.App
import com.tcorner.appbrella.R
import com.tcorner.appbrella.ui.drawer.DrawerActivity
import com.tcorner.appbrella.util.helper.AlarmHelper
import com.tcorner.appbrella.util.helper.PrecipitationHelper
import javax.inject.Inject

/**
 * Sends notification about the status whether you
 * need to bring your umbrella or not.
 */
class NotificationService : BroadcastReceiver(),
    NotificationMvpView {

    companion object {
        private const val CHANNEL_ID = "APPBRELLA_CHANNEL_ID"
        private const val CHANNEL_NAME = "APPBRELLA_CHANNEL_NAME"

        private const val NOTIFICATION_ID = 0
    }

    @Inject
    lateinit var mPresenter: NotificationPresenter

    private lateinit var mContext: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(AlarmHelper.NOTIFICATION_ACTION)) {
            if (context != null) {
                mContext = context

                (context.applicationContext as App).getComponent()!!.inject(this)
                mPresenter.attachView(this)

                mPresenter.getPrecipitation()
            }
        }
    }

    override fun showPrecipitation(precipitation: Int) {
        val notificationIntent = Intent(mContext, DrawerActivity::class.java)

        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(mContext, CHANNEL_ID)
            .setContentIntent(PendingIntent.getActivity(mContext, 0, notificationIntent, 0))
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(mContext.getString(R.string.app_name))
            .setContentText(PrecipitationHelper.getPrecipitationMessage(mContext, precipitation))
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(mChannel)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}