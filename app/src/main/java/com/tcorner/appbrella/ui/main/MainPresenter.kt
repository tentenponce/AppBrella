package com.tcorner.appbrella.ui.main

import com.tcorner.appbrella.common.item.PurchaseItem
import com.tcorner.appbrella.domain.interactor.GetPrecipitationPercentage
import com.tcorner.appbrella.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainPresenter @Inject constructor(val mGetPrecipitationPercentage: GetPrecipitationPercentage) :
    BasePresenter<MainMvpView>() {

    fun getPrecipitation() {
        checkViewAttached()

        mvpView?.showLoading()
        mGetPrecipitationPercentage.execute()
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { compositeDisposable.add(it) }
            .subscribeBy(onNext = {
                mvpView?.hideLoading()
                mvpView?.showPrecipitation(it)
            }, onError = {
                mvpView?.hideLoading()
                mvpView?.getPrecipitationError(it)
            }
            )
    }

    fun successPurchase(toPurchaseItems: List<PurchaseItem>) {
        TODO("consume purchased item")
    }
}