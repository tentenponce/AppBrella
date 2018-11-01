package com.tcorner.appbrella.data.impl

import com.tcorner.appbrella.data.service.BillingService
import com.tcorner.appbrella.domain.model.Product
import com.tcorner.appbrella.domain.repository.BillingRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */

class BillingRepositoryImpl @Inject constructor(val mService: BillingService) : BillingRepository {

    override fun getSkuList(): Single<List<Product>> {
        return mService.getSkuList()
            .flattenAsObservable { it }
            .map { Product(id = it.sku, name = it.title, price = it.price.toDouble()) }
            .toList()
    }
}