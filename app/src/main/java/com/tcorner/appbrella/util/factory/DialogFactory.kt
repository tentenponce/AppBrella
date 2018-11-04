package com.tcorner.appbrella.util.factory

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tcorner.appbrella.R
import kotlinx.android.synthetic.main.layout_loading.view.tv_message

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
object DialogFactory {

    fun createLoadingDialog(context: Context, messageRes: Int): Dialog {
        return createLoadingDialog(context, context.getString(messageRes))
    }

    @SuppressLint("InflateParams")
    fun createLoadingDialog(context: Context, message: String): Dialog {
        val layoutLoading = LayoutInflater.from(context).inflate(R.layout.layout_loading,
            null, false) as LinearLayout

        layoutLoading.tv_message.text = message

        val dialog = Dialog(context)

        dialog.setCancelable(false)
        dialog.setContentView(layoutLoading)

        return dialog
    }
}