package com.tcorner.appbrella.domain.repository

/**
 *
 * Created by Exequiel Egbert V. Ponce on 6/24/2018.
 */
interface DeviceRepository {

    var notificationStatus: Boolean

    fun getDeviceId(): String
}