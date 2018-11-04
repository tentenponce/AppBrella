package com.tcorner.appbrella.ui.drawer

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tcorner.appbrella.R
import com.tcorner.appbrella.ui.base.BaseActivity
import com.tcorner.appbrella.ui.drawer.main.MainFragment
import com.tcorner.appbrella.util.helper.FragmentHelper
import kotlinx.android.synthetic.main.activity_drawer.content_frame
import kotlinx.android.synthetic.main.activity_drawer.drawer_layout
import kotlinx.android.synthetic.main.activity_drawer.nav_view
import kotlinx.android.synthetic.main.activity_drawer.toolbar
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */

class DrawerActivity : BaseActivity(),
    DrawerMvpView,
    NavigationView.OnNavigationItemSelectedListener,
    PurchasesUpdatedListener {

    companion object {

        private const val MAIN = 0
    }

    private var toggle: ActionBarDrawerToggle? = null

    @Inject
    lateinit var mPresenter: DrawerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        activityComponent()?.inject(this)
        mPresenter.attachView(this)

        initViews()
        onNavigationItemSelected(nav_view.menu.getItem(MAIN))
        nav_view.menu.getItem(MAIN).isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)

        if (item.isChecked) { //check if they are on the fragment already
            return false
        }

        when (item.itemId) {
            R.id.nav_main -> FragmentHelper.setupFragment(this,
                MainFragment.newInstance(),
                content_frame,
                getString(R.string.app_name))
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) { //toggle drawer open close
                    drawer_layout.closeDrawer(GravityCompat.START)
                } else {
                    drawer_layout.openDrawer(GravityCompat.START)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * overriding on activity because launch billing flow of google in-app billing
     * does not support fragment... :(
     */
    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        val fragment = FragmentHelper.getFragment(this, content_frame)
        if (fragment != null &&
            fragment is MainFragment) {

            /* but we still pass it to the fragment that is responsible for launching the google in-app billing :P */
            fragment.onPurchasesUpdated(responseCode, purchases)
        }
    }

    private fun initViews() {
        /* toolbar */
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        /* drawer */
        toggle = ActionBarDrawerToggle(this,
            drawer_layout,
            toolbar as Toolbar?,
            R.string.drawer_open,
            R.string.drawer_close)

        if (drawer_layout != null) {
            drawer_layout.addDrawerListener(toggle!!)
            drawer_layout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START) //add shadow
        }

        toggle!!.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }
}