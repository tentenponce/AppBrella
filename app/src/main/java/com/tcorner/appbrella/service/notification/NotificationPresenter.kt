package com.tcorner.appbrella.service.notification

import com.tcorner.appbrella.domain.interactor.GetPrecipitationPercentage
import com.tcorner.appbrella.ui.base.BasePresenter
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class NotificationPresenter
@Inject constructor(
    private val mGetPrecipitationPercentage: GetPrecipitationPercentage) :
    BasePresenter<NotificationMvpView>() {

    companion object {
        const val UNKNOWN_PRECIPITATION = -1
    }

    fun getPrecipitation() {
        checkViewAttached()

        mGetPrecipitationPercentage.execute()
            .doOnSubscribe { compositeDisposable.add(it) }
            .subscribeBy(onSuccess = {
                mvpView?.showPrecipitation(it)
            }, onError = {
                mvpView?.showPrecipitation(UNKNOWN_PRECIPITATION)
            })
    }
}