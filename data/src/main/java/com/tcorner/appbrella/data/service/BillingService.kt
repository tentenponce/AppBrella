package com.tcorner.appbrella.data.service

import android.accounts.NetworkErrorException
import android.app.Activity
import com.android.billingclient.api.*
import com.tcorner.appbrella.domain.common.exception.BillingConnectionException
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/1/2018.
 */
class BillingService @Inject constructor(
    private val mClient: BillingClient,
    private val mActivity: Activity
) {

    /**
     * get the list of all sku with details
     */
    fun getSkuList(): Single<List<SkuDetails>> {
        return Single.create {
            mClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(@BillingClient.BillingResponse billingResponseCode: Int) {
                    if (billingResponseCode == BillingClient.BillingResponse.OK) {
                        val skuList = ArrayList<String>()

                        skuList.add("donation_low")
                        skuList.add("donation_med")
                        skuList.add("donation_high")

                        val params = SkuDetailsParams.newBuilder()
                        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
                        mClient.querySkuDetailsAsync(params.build()) { responseCode, skuDetailsList ->
                            if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
                                if (!it.isDisposed) {
                                    it.onSuccess(skuDetailsList)
                                    mClient.endConnection()
                                }
                            }
                        }
                    } else {
                        if (!it.isDisposed) {
                            it.onError(BillingConnectionException(billingResponseCode))
                            mClient.endConnection()
                        }
                    }
                }

                override fun onBillingServiceDisconnected() {
                    if (!it.isDisposed) {
                        it.onError(NetworkErrorException())
                    }
                }
            })
        }
    }

    /**
     * purchase an item and return its info
     */
    fun purchaseInApp(sku: String): Single<Purchase> {
        return Single.create {
            mClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(responseCode: Int) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSku(sku)
                        .setType(BillingClient.SkuType.INAPP)
                        .build()

                    mClient.launchBillingFlow(mActivity, flowParams) // show google pay

                    /* get the purchased item description */
                    val purchasesResult = mClient.queryPurchases(BillingClient.SkuType.INAPP)
                    if (purchasesResult.responseCode == BillingClient.BillingResponse.OK) {

                        /* find the purchased item on the list */
                        for (purchase in purchasesResult.purchasesList) {
                            if (purchase.sku == sku) {
                                if (!it.isDisposed) {
                                    it.onSuccess(purchase)
                                    mClient.endConnection()
                                    break
                                }
                            }
                        }
                    } else {
                        if (!it.isDisposed) {
                            it.onError(BillingConnectionException(purchasesResult.responseCode))
                            mClient.endConnection()
                        }
                    }
                }

                override fun onBillingServiceDisconnected() {
                    if (!it.isDisposed) {
                        it.onError(NetworkErrorException())
                    }
                }
            })
        }
    }

    /**
     * consume purchased item
     */
    fun consumePurchaseInApp(purchaseToken: String): Completable {
        return Completable.create {
            mClient.consumeAsync(purchaseToken) { responseCode, outToken ->
                if (responseCode == BillingClient.BillingResponse.OK) {
                    it.onComplete()
                } else {
                    if (!it.isDisposed) {
                        it.onError(BillingConnectionException(responseCode))
                        mClient.endConnection()
                    }
                }
            }
        }
    }
}
