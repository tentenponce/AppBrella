package com.tcorner.appbrella.ui.drawer.settings

import com.tcorner.appbrella.ui.base.MvpView

interface SettingsMvpView : MvpView {

    fun setNotificationStatus(status: Boolean)

    fun showError(error: Throwable)

    fun startAlarm()

    fun stopAlarm()
}