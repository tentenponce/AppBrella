package com.tcorner.appbrella.ui.drawer.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tcorner.appbrella.R
import com.tcorner.appbrella.ui.base.BaseFragment
import com.tcorner.appbrella.util.factory.DialogFactory
import com.tcorner.appbrella.util.helper.AlarmHelper
import kotlinx.android.synthetic.main.fragment_settings.const_notification_settings
import kotlinx.android.synthetic.main.fragment_settings.switch_notification_status
import javax.inject.Inject

class SettingsFragment : BaseFragment(),
    SettingsMvpView {

    companion object {
        fun newInstance(): SettingsFragment {
            val fragment = SettingsFragment()

            val bundle = Bundle()
            fragment.arguments = bundle

            return fragment
        }
    }

    @Inject
    lateinit var mPresenter: SettingsPresenter

    private lateinit var mAlarmHelper: AlarmHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAlarmHelper = AlarmHelper(context!!)
        component().inject(this)
        mPresenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switch_notification_status.setOnCheckedChangeListener { _, isChecked ->
            mPresenter.setNotificationStatus(isChecked)
        }

        const_notification_settings.setOnClickListener {
            switch_notification_status.isChecked = !switch_notification_status.isChecked
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mPresenter.getNotificationStatus()
    }

    override fun onDestroyView() {
        mPresenter.detachView()
        super.onDestroyView()
    }

    override fun setNotificationStatus(status: Boolean) {
        switch_notification_status.isChecked = status
    }

    override fun showError(error: Throwable) {
        DialogFactory.createSimpleOkErrorDialog(context!!,
            getString(R.string.error_generic_title),
            String.format(getString(R.string.error_notification_message), error::class.java.simpleName))
            .show()
    }

    override fun startAlarm() {
        mAlarmHelper.start()
    }

    override fun stopAlarm() {
        mAlarmHelper.cancel()
    }

    override fun title() = getString(R.string.app_settings)
}