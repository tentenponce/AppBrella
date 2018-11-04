package com.tcorner.appbrella.ui.drawer.main

import com.tcorner.appbrella.domain.interactor.GetPrecipitationPercentage
import com.tcorner.appbrella.ui.base.BasePresenter
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val mGetPrecipitationPercentage: GetPrecipitationPercentage) :
    BasePresenter<MainMvpView>() {

    fun getPrecipitation() {
        checkViewAttached()

        mvpView?.showLoading()
        mGetPrecipitationPercentage.execute()
            .doOnSubscribe { compositeDisposable.add(it) }
            .subscribeBy(onSuccess = {
                mvpView?.hideLoading()
                mvpView?.showPrecipitation(it)
            }, onError = {
                mvpView?.hideLoading()
                mvpView?.getPrecipitationError(it)
            })
    }
}