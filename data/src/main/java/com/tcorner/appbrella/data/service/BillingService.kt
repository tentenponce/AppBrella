package com.tcorner.appbrella.data.service

import android.accounts.NetworkErrorException
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.tcorner.appbrella.domain.common.exception.BillingConnectionException
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/1/2018.
 */
class BillingService @Inject constructor(
    private val mClient: BillingClient
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
     * consume purchased item
     */
    fun consumeInApp(purchaseToken: String): Completable {
        return Completable.create {
            mClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(responseCode: Int) {
                    mClient.consumeAsync(purchaseToken) { responseCode2, _ ->
                        if (responseCode2 == BillingClient.BillingResponse.OK) {
                            it.onComplete()
                        } else {
                            if (!it.isDisposed) {
                                it.onError(BillingConnectionException(responseCode2))
                                mClient.endConnection()
                            }
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
}
