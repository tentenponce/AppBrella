package com.tcorner.appbrella.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tcorner.appbrella.App
import com.tcorner.appbrella.di.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *
 * Created by Exequiel Egbert V. Ponce on 6/19/2018.
 */
@Module
class AppModule(val app: App) {

    @Provides
    fun provideApp(): App = app

    @Provides
    @AppContext
    fun provideContext(): Context = app

    @Provides
    @Singleton
    internal fun providesRxSharedPreferences(@AppContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
}