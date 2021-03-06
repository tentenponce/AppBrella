package com.tcorner.appbrella.ui.drawer

import com.tcorner.appbrella.domain.interactor.GetNotificationStatus
import com.tcorner.appbrella.ui.base.BasePresenter
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */

class DrawerPresenter
@Inject constructor(private val mGetNotificationStatus: GetNotificationStatus) : BasePresenter<DrawerMvpView>() {

    fun setAlarmNotification() {
        mGetNotificationStatus.execute()
            .doOnSubscribe { compositeDisposable.add(it) }
            .subscribeBy(onSuccess = {
                if (it) {
                    mvpView?.startAlarm()
                } else {
                    mvpView?.stopAlarm()
                }
            }, onError = {
                mvpView?.showError(it)
            })
    }
}