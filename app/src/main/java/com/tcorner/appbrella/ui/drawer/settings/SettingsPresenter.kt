package com.tcorner.appbrella.ui.drawer.settings

import com.tcorner.appbrella.domain.interactor.GetNotificationStatus
import com.tcorner.appbrella.domain.interactor.SetNotificationStatus
import com.tcorner.appbrella.ui.base.BasePresenter
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
    private val mGetNotificationStatus: GetNotificationStatus,
    private val mSetNotificationStatus: SetNotificationStatus
) : BasePresenter<SettingsMvpView>() {

    fun getNotificationStatus() {
        checkViewAttached()
        mGetNotificationStatus.execute()
            .doOnSubscribe { compositeDisposable.add(it) }
            .subscribeBy(onSuccess = {
                mvpView?.setNotificationStatus(it)
            }, onError = {
                mvpView?.showError(it)
            })
    }

    fun setNotificationStatus(status: Boolean) {
        checkViewAttached()

        mSetNotificationStatus.execute(status)
            .doOnSubscribe { compositeDisposable.add(it) }
            .subscribeBy(onSuccess = {
                getNotificationStatus()

                if (it) {
                    mvpView?.startAlarm()
                } else {
                    mvpView?.stopAlarm()
                }

            }, onError = {
                getNotificationStatus()
                mvpView?.showError(it)
            })
    }
}