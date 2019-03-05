package com.tcorner.appbrella.util.helper

import android.content.Context
import com.tcorner.appbrella.R

object PrecipitationHelper {

    fun getPrecipitationMessage(context: Context, precipitation: Int): String =
        when (precipitation) {
            in 0..15 -> context.getString(R.string.no_chance_rain)
            in 16..35 -> context.getString(R.string.low_chance_rain)
            in 36..70 -> context.getString(R.string.medium_chance_rain)
            in 70..100 -> context.getString(R.string.high_chance_rain)
            else -> context.getString(R.string.error_generic)
        }
}