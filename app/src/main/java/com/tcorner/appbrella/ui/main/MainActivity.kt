package com.tcorner.appbrella.ui.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.tcorner.appbrella.R
import com.tcorner.appbrella.ui.base.BaseActivity
import org.jsoup.HttpStatusException
import javax.inject.Inject

class MainActivity : BaseActivity(),
    MainMvpView {

    companion object {
        private const val REQUEST_PERMISSION = 1
        private val PERMISSIONS = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.INTERNET
        )
    }

    @Inject
    lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent()?.inject(this)
        mPresenter.attachView(this)

        mPresenter.getPrecipitation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun showPrecipitation(precipitation: Int?) {
        when (precipitation) {
            in 0..15 -> {
            }
            in 16..35 -> {
            }
            in 36..70 -> {
            }
            in 70..100 -> {
            }
        }
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
