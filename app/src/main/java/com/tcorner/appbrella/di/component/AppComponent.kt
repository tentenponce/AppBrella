package com.tcorner.appbrella.di.component

import android.content.Context
import com.tcorner.appbrella.App
import com.tcorner.appbrella.di.AppContext
import com.tcorner.appbrella.di.module.AppModule
import com.tcorner.appbrella.di.module.DataModule
import com.tcorner.appbrella.di.module.DomainModule
import com.tcorner.appbrella.domain.common.executor.PostExecutionThread
import com.tcorner.appbrella.domain.common.executor.ThreadExecutor
import com.tcorner.appbrella.domain.repository.DeviceRepository
import dagger.Component
import javax.inject.Singleton

/**
 *
 * Created by Exequiel Egbert V. Ponce on 6/17/2018.
 */

@Singleton
@Component(
    modules = [
        AppModule::class,
        DataModule::class,
        DomainModule::class]
)
interface AppComponent {

    @AppContext
    fun context(): Context

    fun app(): App

    fun threadExecutor(): ThreadExecutor

    fun postExecutionThread(): PostExecutionThread

    fun deviceRepository(): DeviceRepository
}