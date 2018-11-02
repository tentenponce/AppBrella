package com.tcorner.appbrella.di.module

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tcorner.appbrella.data.impl.BillingRepositoryImpl
import com.tcorner.appbrella.data.service.BillingService
import com.tcorner.appbrella.di.ActivityContext
import com.tcorner.appbrella.domain.repository.BillingRepository
import dagger.Module
import dagger.Provides

/**
 *
 * Created by Exequiel Egbert V. Ponce on 6/20/2018.
 */
@Module
class ActivityModule(val activity: Activity) {

    @Provides
    fun provideActivity() = activity

    @Provides
    @ActivityContext
    fun provideContext() = activity

    @Provides
    internal fun provideBillingClient(): BillingClient {
        return BillingClient
            .newBuilder(activity)
            .setListener(activity as PurchasesUpdatedListener) // dont handle purchases here
            .build()
    }

    @Provides
    internal fun provideBillingService(billingClient: BillingClient): BillingService {
        return BillingService(
            billingClient
        )
    }

    @Provides
    internal fun billingRepository(billingService: BillingService): BillingRepository {
        return BillingRepositoryImpl(billingService)
    }
}