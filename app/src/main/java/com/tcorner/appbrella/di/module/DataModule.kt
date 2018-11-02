package com.tcorner.appbrella.di.module

import android.content.Context
import com.tcorner.appbrella.data.service.LocationService
import com.tcorner.appbrella.data.service.WeatherService
import com.tcorner.appbrella.di.AppContext
import com.tcorner.appbrella.util.factory.SslClientFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *
 * Created by Exequiel Egbert V. Ponce on 6/24/2018.
 */
@Module
class DataModule {

    @Provides
    @Singleton
    internal fun provideHttpClient(): OkHttpClient {
        val builder = SslClientFactory.okHttpClientBuilder
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)

        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideLocationService(@AppContext context: Context): LocationService {
        return LocationService(context)
    }

    @Provides
    @Singleton
    internal fun provideWeatherService(): WeatherService {
        return WeatherService()
    }
}