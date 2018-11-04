package com.tcorner.appbrella.ui.drawer.main

import com.tcorner.appbrella.domain.model.PurchaseProduct
import com.tcorner.appbrella.ui.base.MvpView

interface MainMvpView : MvpView {

    fun getPrecipitationError(e: Throwable)

    fun showPrecipitation(precipitation: Int?)

    fun showLoading()

    fun hideLoading()

    fun successPurchase(purchaseProducts: List<PurchaseProduct>)

    fun errorPurchase(e: Throwable)
}