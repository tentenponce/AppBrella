package com.tcorner.appbrella.util.helper

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */

class FragmentHelper {

    companion object {
        fun setupFragment(activity: AppCompatActivity, fragment: Fragment, frameLayout: FrameLayout, title: String) {
            activity.supportFragmentManager
                .beginTransaction()
                .replace(frameLayout.id, fragment)
                .commit()
        }

        fun getFragment(activity: AppCompatActivity, frameLayout: FrameLayout): Fragment? =
            activity.supportFragmentManager
                .findFragmentById(frameLayout.id)

    }
}