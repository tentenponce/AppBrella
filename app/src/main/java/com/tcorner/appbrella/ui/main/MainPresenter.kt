package com.tcorner.appbrella.ui.main

import com.tcorner.appbrella.domain.interactor.ConsumeDonation
import com.tcorner.appbrella.domain.interactor.GetPrecipitationPercentage
import com.tcorner.appbrella.ui.base.BasePresenter
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class MainPresenter @Inject constructor(
    val mGetPrecipitationPercentage: GetPrecipitationPercentage,
    val mConsumeDonation: ConsumeDonation
) :
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

    fun consumePurchases(purchaseTokens: MutableList<String>) {
        mConsumeDonation.execute(purchaseTokens)
            .subscribeBy(onComplete = {
                mvpView?.successPurchase()
            }, onError = {
                mvpView?.errorPurchase(it)
            })
    }
}