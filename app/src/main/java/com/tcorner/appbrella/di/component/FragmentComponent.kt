package com.tcorner.appbrella.di.component

import com.tcorner.appbrella.di.PerFragment
import com.tcorner.appbrella.di.module.FragmentModule
import com.tcorner.appbrella.ui.drawer.donate.DonateFragment
import com.tcorner.appbrella.ui.drawer.main.MainFragment
import com.tcorner.appbrella.ui.drawer.settings.SettingsFragment
import dagger.Subcomponent

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
@PerFragment
@Subcomponent(modules = [(FragmentModule::class)])
interface FragmentComponent {

    fun inject(mainFragment: MainFragment)

    fun inject(donateFragment: DonateFragment)

    fun inject(settingsFragment: SettingsFragment)
}