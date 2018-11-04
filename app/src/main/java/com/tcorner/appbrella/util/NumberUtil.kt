package com.tcorner.appbrella.util

import java.text.DecimalFormat

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
object NumberUtil {

    val PESO_SIGN = "\u20B1"

    fun formatTwoDecimal(d: Double): String {
        val numberFormat = DecimalFormat("#,##0.00")

        return numberFormat.format(d)
    }

    fun formatTwoDecimalWithPesoSign(d: Double): String =
        String.format("%s %s", NumberUtil.PESO_SIGN, NumberUtil.formatTwoDecimal(d))
}