package com.tcorner.appbrella.data.impl

import com.tcorner.appbrella.data.service.BillingService
import com.tcorner.appbrella.domain.model.Donation
import com.tcorner.appbrella.domain.repository.BillingRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */

class BillingRepositoryImpl @Inject constructor(val mService: BillingService) : BillingRepository {

    override fun consumeInApp(purchaseToken: String): Completable {
        return mService.consumeInApp(purchaseToken)
    }

    override fun getSkuList(): Single<List<Donation>> {
        return mService.getSkuList()
            .flattenAsObservable { it }
            .map {
                Donation(id = it.sku,
                    name = it.title.replace("\\(.*\\)".toRegex(), "").trim(), // trim the app name. eg: "Umbre (AppBrella)" to "Umbre"
                    description = it.description,
                    price = it.price)
            }
            .toList()
    }
}