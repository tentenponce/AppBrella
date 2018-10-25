package com.tcorner.appbrella.di.component

import com.tcorner.appbrella.di.PerActivity
import com.tcorner.appbrella.di.module.ActivityModule
import dagger.Subcomponent

/**
 *
 * Created by Exequiel Egbert V. Ponce on 6/20/2018.
 */
@PerActivity
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {
}