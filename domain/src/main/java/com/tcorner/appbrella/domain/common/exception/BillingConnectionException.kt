package com.tcorner.appbrella.domain.common.exception

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */
class BillingConnectionException constructor(val errorCode: Int) : RuntimeException() {
}