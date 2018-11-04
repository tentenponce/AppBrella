package com.tcorner.appbrella.ui.drawer.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tcorner.appbrella.R
import com.tcorner.appbrella.domain.common.exception.LocationException
import com.tcorner.appbrella.ui.base.BaseFragment
import com.tcorner.appbrella.util.AnimateUtil
import com.tcorner.appbrella.util.LoadingMessageUtil
import kotlinx.android.synthetic.main.fragment_main.const_main
import kotlinx.android.synthetic.main.fragment_main.iv_umbrella_off
import kotlinx.android.synthetic.main.fragment_main.iv_umbrella_on
import kotlinx.android.synthetic.main.fragment_main.tv_message
import kotlinx.android.synthetic.main.fragment_main.tv_sub_message
import org.jsoup.HttpStatusException
import javax.inject.Inject

class MainFragment : BaseFragment(),
    MainMvpView {
    companion object {

        fun newInstance(): MainFragment {
            val fragment = MainFragment()

            val bundle = Bundle()
            fragment.arguments = bundle

            return fragment
        }

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

    private var mIsLoading: Boolean = false // to check if still loading to continue the loading loop

    private var mLoadingHandler: Handler = Handler()

    private lateinit var mLoadingTextRunnable: Runnable // handle loading text animation loop

    private lateinit var mLoadingImageRunnable: Runnable // handle loading text animation loop

    private var mIsOpen: Boolean = false // for animation
    private var mPreviousLoadingMessage: String = ""

    override fun title(): String {
        return getString(R.string.menu_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component().inject(this)
        mPresenter.attachView(this)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        const_main.setOnClickListener {
            mPresenter.getPrecipitation()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mPresenter.getPrecipitation()
    }

    override fun onPause() {
        super.onPause()

        mLoadingHandler.removeCallbacks(mLoadingTextRunnable)
        mLoadingHandler.removeCallbacks(mLoadingImageRunnable)
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
            is LocationException -> tv_message.text = e.message
            else -> tv_message.text = String.format(
                getString(
                    R.string.error_generic, e.javaClass.simpleName
                )
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val permissionsMap = HashMap<String, Int>()
        for ((index, permission) in permissions.withIndex()) {
            permissionsMap[permission] = grantResults[index]
        }

        if (hasPermissions()) {
            if (activity != null &&
                activity is MainInterface) {
                (activity as MainInterface).recreateMainFragment()
            }
        }
    }

    private fun hasPermissions(): Boolean {
        for (permission in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(activity!!, PERMISSIONS, REQUEST_PERMISSION)
    }

    private fun getNoRepeatRandomLoadingMessage(): String {
        var loadingMessage = LoadingMessageUtil.getRandomLoadingMessage(context!!)

        while (loadingMessage == mPreviousLoadingMessage) {
            loadingMessage = LoadingMessageUtil.getRandomLoadingMessage(context!!)
        }

        mPreviousLoadingMessage = loadingMessage

        return loadingMessage
    }
}
