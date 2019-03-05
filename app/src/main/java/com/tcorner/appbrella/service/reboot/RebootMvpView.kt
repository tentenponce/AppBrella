package com.tcorner.appbrella.service.reboot

import com.tcorner.appbrella.ui.base.MvpView

interface RebootMvpView : MvpView {
    fun startAlarm()
}