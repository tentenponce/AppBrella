package com.tcorner.appbrella.di.component

import com.tcorner.appbrella.di.PerFragment
import com.tcorner.appbrella.di.module.FragmentModule
import com.tcorner.appbrella.ui.drawer.main.MainFragment
import dagger.Subcomponent

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
@PerFragment
@Subcomponent(modules = [(FragmentModule::class)])
interface FragmentComponent {

    fun inject(mainFragment: MainFragment)
}