package com.tcorner.appbrella.data.service

import android.accounts.NetworkErrorException
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.tcorner.appbrella.domain.common.exception.BillingConnectionException
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Single
import io.reactivex.SingleEmitter
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
            if (mClient.isReady) {
                getAllDonations(it)
            } else {
                mClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(@BillingClient.BillingResponse billingResponseCode: Int) {
                        if (billingResponseCode == BillingClient.BillingResponse.OK) {
                            getAllDonations(it)
                        } else {
                            if (!it.isDisposed) {
                                it.onError(BillingConnectionException(billingResponseCode))
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

    /**
     * consume purchased item
     */
    fun consumeInApp(purchaseToken: String): Completable {
        return Completable.create {
            if (mClient.isReady) {
                consumeInApp(purchaseToken, it)
            } else {
                mClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(responseCode: Int) {
                        if (responseCode == BillingClient.BillingResponse.OK) {
                            consumeInApp(purchaseToken, it)
                        } else {
                            if (!it.isDisposed) {
                                it.onError(BillingConnectionException(responseCode))
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

    private fun getAllDonations(emitter: SingleEmitter<List<SkuDetails>>) {
        val skuList = ArrayList<String>()

        skuList.add("donation_low")
        skuList.add("donation_med")
        skuList.add("donation_high")

        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        mClient.querySkuDetailsAsync(params.build()) { responseCode, skuDetailsList ->
            if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
                if (!emitter.isDisposed) {
                    emitter.onSuccess(skuDetailsList)
                }
            }
        }
    }

    private fun consumeInApp(purchaseToken: String, completableEmitter: CompletableEmitter) {
        mClient.consumeAsync(purchaseToken) { responseCode2, _ ->
            if (responseCode2 == BillingClient.BillingResponse.OK) {
                completableEmitter.onComplete()
            } else {
                if (!completableEmitter.isDisposed) {
                    completableEmitter.onError(BillingConnectionException(responseCode2))
                }
            }
        }
    }
}
