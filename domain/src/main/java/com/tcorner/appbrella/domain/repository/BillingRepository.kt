package com.tcorner.appbrella.domain.repository

import com.tcorner.appbrella.domain.model.Donation
import io.reactivex.Completable
import io.reactivex.Single

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */
interface BillingRepository {

    fun getSkuList(): Single<List<Donation>>

    fun consumeInApp(purchaseToken: String): Completable
}