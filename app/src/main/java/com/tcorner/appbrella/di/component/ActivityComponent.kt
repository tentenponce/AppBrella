package com.tcorner.appbrella.di.component

import com.tcorner.appbrella.di.PerActivity
import com.tcorner.appbrella.di.module.ActivityModule
import com.tcorner.appbrella.di.module.FragmentModule
import com.tcorner.appbrella.ui.drawer.DrawerActivity
import dagger.Subcomponent

/**
 *
 * Created by Exequiel Egbert V. Ponce on 6/20/2018.
 */
@PerActivity
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {

    fun plus(module: FragmentModule): FragmentComponent

    fun inject(drawerActivity: DrawerActivity)
}