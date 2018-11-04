package com.tcorner.appbrella.ui.drawer.donate

import com.tcorner.appbrella.domain.interactor.ConsumeDonation
import com.tcorner.appbrella.domain.interactor.GetDonations
import com.tcorner.appbrella.domain.model.PurchaseDonation
import com.tcorner.appbrella.ui.base.BasePresenter
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
class DonatePresenter @Inject constructor(
    private val mGetDonations: GetDonations,
    private val mConsumeDonation: ConsumeDonation) : BasePresenter<DonateMvpView>() {

    fun getDonations() {
        checkViewAttached()
        mvpView?.showSwipeLoading()

        mGetDonations.execute()
            .doOnSubscribe { compositeDisposable.add(it) }
            .subscribeBy(onSuccess = {
                mvpView?.hideSwipeLoading()
                mvpView?.showDonations(it)
            }, onError = {
                mvpView?.hideSwipeLoading()
                mvpView?.showGetDonationError(it)
            })
    }

    fun consumePurchases(purchaseDonations: List<PurchaseDonation>) {
        checkViewAttached()
        mvpView?.showLoading()

        mConsumeDonation.execute(purchaseDonations)
            .subscribeBy(onComplete = {
                mvpView?.hideLoading()
                mvpView?.successPurchase(purchaseDonations)
            }, onError = {
                mvpView?.hideLoading()
                mvpView?.errorPurchase(it)
            })
    }
}