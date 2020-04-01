package com.stetter.escambo.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.stetter.escambo.ui.dialog.LoadingDialog

open class BaseActivity : AppCompatActivity() {
    private val loadingDialog by lazy { LoadingDialog(this) }




    open fun onStartLoading() {
        loadingDialog.show()
    }

    open fun onStopLoading() {
        loadingDialog.dismiss()
    }
}