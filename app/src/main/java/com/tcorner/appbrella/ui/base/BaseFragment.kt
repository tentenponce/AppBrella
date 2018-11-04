package com.tcorner.appbrella.ui.base

import android.support.v4.app.Fragment

import com.tcorner.appbrella.di.component.ActivityComponent
import com.tcorner.appbrella.di.component.FragmentComponent
import com.tcorner.appbrella.di.module.FragmentModule

/**
 * Base Fragment
 * Created by Tenten Ponce on 8/30/2017.
 */

abstract class BaseFragment : Fragment(), MvpView {

    private var mComponent: FragmentComponent? = null

    private fun activityComponent(): ActivityComponent? {
        if (activity is BaseActivity) {
            return (activity as BaseActivity).activityComponent()
        }

        throw ComponentNotFound()
    }

    fun component(): FragmentComponent {
        if (mComponent == null) {
            mComponent = activityComponent()?.plus(FragmentModule(this))
        }

        return mComponent as FragmentComponent
    }

    abstract fun title(): String

    private class ComponentNotFound : RuntimeException("Fragment activity should extend BaseActivity before requesting component")
}
