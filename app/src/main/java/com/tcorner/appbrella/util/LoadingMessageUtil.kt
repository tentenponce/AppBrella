package com.tcorner.appbrella.util

import android.content.Context
import com.tcorner.appbrella.R

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
object LoadingMessageUtil {

    fun getRandomLoadingMessage(context: Context): String {
        var loadingMessage = ""

        when ((1..5).random()) {
            1 -> loadingMessage = context.getString(R.string.loading_1)
            2 -> loadingMessage = context.getString(R.string.loading_2)
            3 -> loadingMessage = context.getString(R.string.loading_3)
            4 -> loadingMessage = context.getString(R.string.loading_4)
            5 -> loadingMessage = context.getString(R.string.loading_5)
        }

        return loadingMessage
    }
}