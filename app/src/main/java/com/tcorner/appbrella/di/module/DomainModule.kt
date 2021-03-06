package com.tcorner.appbrella.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tcorner.appbrella.data.common.executor.JobExecutor
import com.tcorner.appbrella.data.impl.DeviceRepositoryImpl
import com.tcorner.appbrella.data.impl.LocationRepositoryImpl
import com.tcorner.appbrella.data.impl.WeatherRepositoryImpl
import com.tcorner.appbrella.data.service.LocationService
import com.tcorner.appbrella.data.service.WeatherService
import com.tcorner.appbrella.di.AppContext
import com.tcorner.appbrella.domain.common.executor.PostExecutionThread
import com.tcorner.appbrella.domain.common.executor.ThreadExecutor
import com.tcorner.appbrella.domain.repository.DeviceRepository
import com.tcorner.appbrella.domain.repository.LocationRepository
import com.tcorner.appbrella.domain.repository.WeatherRepository
import com.tcorner.appbrella.util.provider.ThreadProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *
 * Created by Exequiel Egbert V. Ponce on 6/24/2018.
 */

@Module
class DomainModule {

    /* Executor */
    @Provides
    @Singleton
    internal fun providesThreadExecutor(): ThreadExecutor {
        return JobExecutor()
    }

    @Provides
    @Singleton
    internal fun postExecutionThread(): PostExecutionThread {
        return ThreadProvider()
    }

    /* Repository */
    @Provides
    @Singleton
    internal fun deviceRepository(@AppContext context: Context, sharedPreferences: SharedPreferences): DeviceRepository {
        return DeviceRepositoryImpl(context, sharedPreferences)
    }

    @Provides
    @Singleton
    internal fun locationRepository(locationService: LocationService): LocationRepository {
        return LocationRepositoryImpl(locationService)
    }

    @Provides
    @Singleton
    internal fun weatherRepository(weatherService: WeatherService): WeatherRepository {
        return WeatherRepositoryImpl(weatherService)
    }
}