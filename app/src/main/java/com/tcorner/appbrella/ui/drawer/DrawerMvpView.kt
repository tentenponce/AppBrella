package com.tcorner.appbrella.ui.drawer

import com.tcorner.appbrella.ui.base.MvpView

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
interface DrawerMvpView : MvpView {

    fun startAlarm()

    fun stopAlarm()

    fun showError(error: Throwable)
}