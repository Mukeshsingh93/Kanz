package com.example.myapplication.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.myapplication.R
import android.widget.ProgressBar
import com.github.ybq.android.spinkit.SpinKitView


class ProgressDialog(context: Context) {
    private val dialog: Dialog
    private val mprogressBar: SpinKitView

    init {
        val progressBar = ProgressBar(context)
       // dialog = Dialog(context, R.style.ProgressDialog)
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.prograss_bar_dialog)
        dialog.getWindow()!!.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        mprogressBar = dialog.findViewById(R.id.spin_kit) as SpinKitView
        dialog.setCancelable(false)
    }

    private fun show() {
        if (!dialog.isShowing)
            dialog.show()
    }

    private fun dismiss() {
        if (dialog.isShowing)
            dialog.dismiss()
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading)
            show()
        else
            dismiss()
    }
}
