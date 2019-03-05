package com.tcorner.appbrella.service.reboot

import android.util.Log
import com.tcorner.appbrella.domain.interactor.GetNotificationStatus
import com.tcorner.appbrella.ui.base.BasePresenter
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class RebootPresenter
@Inject constructor(private val mGetNotificationStatus: GetNotificationStatus) :
    BasePresenter<RebootMvpView>() {

    fun getNotificationStatus() {
        checkViewAttached()
        mGetNotificationStatus.execute()
            .doOnSubscribe { compositeDisposable.add(it) }
            .subscribeBy(onSuccess = {
                if (it) {
                    mvpView?.startAlarm()
                }
            }, onError = {
                Log.e("androidruntime", "AppBrella: problem starting alarm ${it::class.java.simpleName}")
            })
    }
}