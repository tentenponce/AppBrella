package com.tcorner.appbrella.ui.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tcorner.appbrella.R
import com.tcorner.appbrella.domain.model.PurchaseProduct
import com.tcorner.appbrella.ui.base.BaseActivity
import com.tcorner.appbrella.util.AnimateUtil
import com.tcorner.appbrella.util.mapper.PurchaseMapper
import com.tcorner.appbrella.util.random
import kotlinx.android.synthetic.main.activity_main.const_main
import kotlinx.android.synthetic.main.activity_main.iv_donate
import kotlinx.android.synthetic.main.activity_main.iv_umbrella_off
import kotlinx.android.synthetic.main.activity_main.iv_umbrella_on
import kotlinx.android.synthetic.main.activity_main.tv_message
import kotlinx.android.synthetic.main.activity_main.tv_sub_message
import org.jsoup.HttpStatusException
import javax.inject.Inject

class MainActivity : BaseActivity(),
    MainMvpView,
    PurchasesUpdatedListener {
    companion object {

        private const val REQUEST_PERMISSION = 1

        private const val LOADING_TEXT_SWITCH_TIMER = 1500L

        private const val LOADING_IMAGE_TIMER = 700L
        private val PERMISSIONS = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.INTERNET
        )
    }

    @Inject
    lateinit var mPresenter: MainPresenter

    @Inject
    lateinit var mBillingClient: BillingClient

    private var mIsLoading: Boolean = false // to check if still loading to continue the loading loop

    private var mLoadingHandler: Handler = Handler()

    private lateinit var mLoadingTextRunnable: Runnable // handle loading text animation loop

    private lateinit var mLoadingImageRunnable: Runnable // handle loading text animation loop
    private var mIsOpen: Boolean = false // for animation
    private var mPreviousLoadingMessage: String = ""
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
        mLoadingHandler.removeCallbacks(mLoadingTextRunnable)
        mLoadingHandler.removeCallbacks(mLoadingImageRunnable)
        mIsLoading = false
    }

    override fun showLoading() {
        if (!mIsLoading) {
            mIsOpen = !mIsOpen
            AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, mIsOpen)
            tv_message.text = getNoRepeatRandomLoadingMessage()

            mLoadingHandler.postDelayed(mLoadingTextRunnable, LOADING_TEXT_SWITCH_TIMER)
            mLoadingHandler.postDelayed(mLoadingImageRunnable, LOADING_IMAGE_TIMER)
        }

        mIsLoading = true

        tv_sub_message.text = ""
    }

    override fun showPrecipitation(precipitation: Int?) {
        when (precipitation) {
            in 0..15 -> {
                tv_message.setText(R.string.no_chance_rain)
                AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, false)
                mIsOpen = false
            }
            in 16..35 -> {
                tv_message.setText(R.string.low_chance_rain)
                AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, false)
                mIsOpen = false
            }
            in 36..70 -> {
                tv_message.setText(R.string.medium_chance_rain)
                AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, true)
                mIsOpen = true
            }
            in 70..100 -> {
                tv_message.setText(R.string.high_chance_rain)
                AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, true)
                mIsOpen = true
            }
        }

        tv_sub_message.text = String.format(getString(R.string.rain_chance), precipitation.toString())
    }

    override fun getPrecipitationError(e: Throwable) {
        when (e) {
            is SecurityException -> requestPermission()
            is HttpStatusException -> tv_message.setText(R.string.error_network)
            else -> tv_message.text = String.format(
                getString(
                    R.string.error_generic, e.javaClass.simpleName
                )
            )
        }
    }

    override fun errorPurchase(e: Throwable) {
        Toast.makeText(
            this,
            String.format(getString(R.string.error_donation), e.javaClass.simpleName),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun successPurchase(purchaseProducts: List<PurchaseProduct>) {
        AlertDialog.Builder(this)
            .setTitle(R.string.success)
            .setMessage(R.string.success_donation)
            .setPositiveButton(R.string.yes) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    /**
     * Consume the purchased item/s immediately
     */
    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            mPresenter.consumePurchases(PurchaseMapper.toPurchaseProducts(purchases))
        } else if (responseCode == BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED) {
            Toast.makeText(this, R.string.error_donation_feature, Toast.LENGTH_LONG).show()
        } else if (responseCode == BillingClient.BillingResponse.SERVICE_DISCONNECTED ||
            responseCode == BillingClient.BillingResponse.SERVICE_UNAVAILABLE ||
            responseCode == BillingClient.BillingResponse.ERROR) {
            Toast.makeText(this, R.string.error_donation_service, Toast.LENGTH_LONG).show()
//        } else if (responseCode == BillingClient.BillingResponse.BILLING_UNAVAILABLE) {
            // api version surely support this
//        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // do nothing when purchase is cancelled
//        } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            // this won't happen as we are consuming if item already owned
//        } else if (responseCode == BillingClient.BillingResponse.ITEM_NOT_OWNED) {
            // this won't happen as we are purchasing before consuming
        } else {
            Toast.makeText(this, getString(R.string.error_generic, responseCode.toString()), Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val permissionsMap = HashMap<String, Int>()
        for ((index, permission) in permissions.withIndex()) {
            permissionsMap[permission] = grantResults[index]
        }

        if (hasPermissions()) {
            recreate()
        }
    }

    private fun init() {
        mLoadingTextRunnable = Runnable {
            tv_message.text = getNoRepeatRandomLoadingMessage()

            if (mIsLoading) {
                mLoadingHandler.postDelayed(mLoadingTextRunnable, LOADING_TEXT_SWITCH_TIMER)
            }
        }

        mLoadingImageRunnable = Runnable {
            mIsOpen = !mIsOpen
            AnimateUtil.animateFadeInFadeOut(iv_umbrella_on, iv_umbrella_off, mIsOpen)

            if (mIsLoading) {
                mLoadingHandler.postDelayed(mLoadingImageRunnable, LOADING_IMAGE_TIMER)
            }
        }
    }

    private fun initViews() {
        const_main.setOnClickListener {
            mPresenter.getPrecipitation()
        }

        iv_donate.setOnClickListener {
            mBillingClient.startConnection(object : BillingClientStateListener {

                override fun onBillingServiceDisconnected() {
                }

                override fun onBillingSetupFinished(responseCode: Int) {
                    mBillingClient.launchBillingFlow(
                        this@MainActivity, BillingFlowParams.newBuilder()
                        .setSku("donation_low")
                        .setType(BillingClient.SkuType.INAPP)
                        .build()
                    )
                }
            })
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

    private fun getNoRepeatRandomLoadingMessage(): String {
        var loadingMessage = getRandomLoadingMessage()

        while (loadingMessage == mPreviousLoadingMessage) {
            loadingMessage = getRandomLoadingMessage()
        }

        mPreviousLoadingMessage = loadingMessage

        return loadingMessage
    }

    private fun getRandomLoadingMessage(): String {
        var loadingMessage = ""

        when ((1..5).random()) {
            1 -> loadingMessage = getString(R.string.loading_1)
            2 -> loadingMessage = getString(R.string.loading_2)
            3 -> loadingMessage = getString(R.string.loading_3)
            4 -> loadingMessage = getString(R.string.loading_4)
            5 -> loadingMessage = getString(R.string.loading_5)
        }

        return loadingMessage
    }
}
