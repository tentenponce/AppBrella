package com.tcorner.appbrella.service.notification

import com.tcorner.appbrella.ui.base.MvpView

interface NotificationMvpView: MvpView {

    fun showPrecipitation(precipitation: Int)
}