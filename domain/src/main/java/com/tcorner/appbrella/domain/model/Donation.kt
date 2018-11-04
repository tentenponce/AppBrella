package com.tcorner.appbrella.domain.model

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */

data class Donation(
    val id: String,
    val name: String,
    val description: String,
    val price: String // sku returns a string with the currency in it, TODO should also save the country currency and price AS double
)