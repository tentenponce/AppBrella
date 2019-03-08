package com.tcorner.appbrella.service.reboot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tcorner.appbrella.App
import com.tcorner.appbrella.util.helper.AlarmHelper
import javax.inject.Inject

/**
 * Start notification service once device reboots.
 */
class RebootService : BroadcastReceiver(),
    RebootMvpView {
    @Inject
    lateinit var mPresenter: RebootPresenter

    private lateinit var mContext: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            if (context != null) {
                mContext = context
                (context.applicationContext as App).getComponent()!!.inject(this)

                mPresenter.attachView(this)

                mPresenter.getNotificationStatus()
            }
        }
    }

    override fun startAlarm() {
        AlarmHelper(mContext).start()
    }
}