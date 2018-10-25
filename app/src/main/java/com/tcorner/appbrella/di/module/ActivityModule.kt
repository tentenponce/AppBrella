package com.tcorner.appbrella.di.module

import android.app.Activity
import com.tcorner.appbrella.di.ActivityContext
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
}