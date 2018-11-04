package com.tcorner.appbrella.ui.drawer.donate

import com.tcorner.appbrella.domain.model.Donation
import com.tcorner.appbrella.domain.model.PurchaseDonation
import com.tcorner.appbrella.ui.base.MvpView

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
interface DonateMvpView : MvpView {

    fun showSwipeLoading()

    fun hideSwipeLoading()

    fun showDonations(donations: List<Donation>)

    fun showGetDonationError(it: Throwable)

    fun successPurchase(purchaseDonations: List<PurchaseDonation>)

    fun errorPurchase(e: Throwable)

    fun showLoading()

    fun hideLoading()
}