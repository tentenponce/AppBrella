package com.tcorner.appbrella

import android.app.Application
import android.content.Context
import com.tcorner.appbrella.di.component.AppComponent
import com.tcorner.appbrella.di.component.DaggerAppComponent
import com.tcorner.appbrella.di.module.AppModule

/**
 *
 * Created by Exequiel Egbert V. Ponce on 6/17/2018.
 */
class App : Application() {

    internal var mApplicationComponent: AppComponent? = null

    companion object {
        fun get(context: Context) = context.applicationContext as App
    }

    fun getComponent(): AppComponent? {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        }

        return mApplicationComponent
    }
}