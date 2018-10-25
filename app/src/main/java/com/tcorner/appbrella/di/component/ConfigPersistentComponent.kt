package com.tcorner.appbrella.di.component

import com.tcorner.appbrella.di.ConfigPersistent
import com.tcorner.appbrella.di.component.ActivityComponent
import com.tcorner.appbrella.di.component.AppComponent
import com.tcorner.appbrella.di.module.ActivityModule
import dagger.Component

/**
 * A dagger component that will live during the lifecycle of an Activity but it won't
 * be destroy during configuration changes. Check [BaseActivity] to see how this components
 * survives configuration changes.
 * Use the [ConfigPersistent] scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = [(AppComponent::class)])
interface ConfigPersistentComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

}