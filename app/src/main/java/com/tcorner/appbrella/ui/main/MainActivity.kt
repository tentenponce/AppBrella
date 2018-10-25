package com.tcorner.appbrella.ui.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.tcorner.appbrella.R
import com.tcorner.appbrella.ui.base.BaseActivity
import com.tcorner.appbrella.util.AnimateUtil
import com.tcorner.appbrella.util.random
import kotlinx.android.synthetic.main.activity_main.const_main
import kotlinx.android.synthetic.main.activity_main.iv_umbrella_off
import kotlinx.android.synthetic.main.activity_main.iv_umbrella_on
import kotlinx.android.synthetic.main.activity_main.tv_message
import kotlinx.android.synthetic.main.activity_main.tv_sub_message
import org.jsoup.HttpStatusException
import javax.inject.Inject

class MainActivity : BaseActivity(),
        MainMvpView {

    companion object {
        private const val REQUEST_PERMISSION = 1
        private const val LOADING_TEXT_SWITCH_TIMER = 700L
        private val PERMISSIONS = arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.INTERNET
        )
    }

    @Inject
    lateinit var mPresenter: MainPresenter

    private var isLoading: Boolean = false // to check if still loading to continue the loading loop
    private var loadingHandler: Handler = Handler()
    private lateinit var loadingRunnable: Runnable // handle loading text animation loop
    private var isOpen: Boolean = false // for animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent()?.inject(this)
        mPresenter.attachView(this)

        init()
        initViews()
        mPresenter.getPrecipitation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun hideLoading() {
        loadingHandler.removeCallbacks(loadingRunnable)
        isLoading = false
    }

    override fun showLoading() {
        if (!isLoading) {
            isOpen = !isOpen
            AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, isOpen)

            loadingHandler.postDelayed(loadingRunnable, LOADING_TEXT_SWITCH_TIMER)
        }

        isLoading = true

        tv_sub_message.text = ""
    }

    override fun showPrecipitation(precipitation: Int?) {
        when (precipitation) {
            in 0..15 -> {
                tv_message.setText(R.string.no_chance_rain)
                AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, false)
                isOpen = false
            }
            in 16..35 -> {
                tv_message.setText(R.string.low_chance_rain)
                AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, false)
                isOpen = false
            }
            in 36..70 -> {
                tv_message.setText(R.string.medium_chance_rain)
                AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, true)
                isOpen = true
            }
            in 70..100 -> {
                tv_message.setText(R.string.high_chance_rain)
                AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, true)
                isOpen = true
            }
        }

        tv_sub_message.text = String.format(getString(R.string.rain_chance), precipitation.toString())
    }

    override fun getPrecipitationError(e: Throwable?) {
        Log.e("androidruntime", "e?.javaClass?.simpleName: ${e?.javaClass?.simpleName}")
        Log.e("androidruntime", "e?.message: ${e?.message}")

        if (e is SecurityException) {
            requestPermission()
        } else if (e is HttpStatusException) {
            Log.e("androidruntime", "e?.status: ${e.statusCode}")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        var index = 0
        val PermissionsMap = HashMap<String, Int>()
        for (permission in permissions) {
            PermissionsMap[permission] = grantResults[index]
            index++
        }

        if (hasPermissions()) {
            recreate()
        }
    }

    private fun init() {
        loadingRunnable = Runnable {
            isOpen = !isOpen
            AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, isOpen)

            val loadingTextId = (1..5).random()

            when (loadingTextId) {
                1 -> tv_message.setText(R.string.loading_1)
                2 -> tv_message.setText(R.string.loading_2)
                3 -> tv_message.setText(R.string.loading_3)
                4 -> tv_message.setText(R.string.loading_4)
                5 -> tv_message.setText(R.string.loading_5)
            }

            if (isLoading) {
                loadingHandler.postDelayed(loadingRunnable, LOADING_TEXT_SWITCH_TIMER)
            }
        }
    }

    private fun initViews() {
        const_main.setOnClickListener {
            mPresenter.getPrecipitation()
        }
    }

    private fun hasPermissions(): Boolean {
        for (permission in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION)
    }
}
