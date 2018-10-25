package com.tcorner.appbrella.di.module

import android.content.Context
import com.tcorner.appbrella.App
import com.tcorner.appbrella.di.AppContext
import dagger.Module
import dagger.Provides

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
}