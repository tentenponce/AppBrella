package com.tcorner.appbrella.data.impl

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import com.tcorner.appbrella.domain.repository.DeviceRepository
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 6/24/2018.
 */
class DeviceRepositoryImpl @Inject constructor(
    private val mContext: Context,
    private val mSharedPreferences: SharedPreferences
) : DeviceRepository {

    companion object {
        private const val NOTIFICATION_STATUS = "NOTIFICATION_STATUS"
    }

    override var notificationStatus: Boolean
        get() = mSharedPreferences.getBoolean(NOTIFICATION_STATUS, true)
        set(value) = mSharedPreferences.edit().putBoolean(NOTIFICATION_STATUS, value).apply()

    @SuppressLint("HardwareIds")
    override fun getDeviceId(): String {
        return Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID)
    }
}