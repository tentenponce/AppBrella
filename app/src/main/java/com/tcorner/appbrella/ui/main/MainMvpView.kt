package com.tcorner.appbrella.ui.main

import com.tcorner.appbrella.ui.base.MvpView

interface MainMvpView : MvpView {

    fun getPrecipitationError(e: Throwable?)

    fun showPrecipitation(precipitation: Int?)

    fun showLoading()

    fun hideLoading()
}