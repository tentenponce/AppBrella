package com.tcorner.appbrella.di.module

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.tcorner.appbrella.di.ActivityContext
import dagger.Module
import dagger.Provides

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
@Module
class FragmentModule(private val mFragment: Fragment) {

    @Provides
    fun activity(): Activity? {
        return mFragment.activity
    }

    @Provides
    fun fragment(): Fragment {
        return mFragment
    }

    @Provides
    @ActivityContext
    fun context(activity: Activity): Context {
        return activity
    }
}