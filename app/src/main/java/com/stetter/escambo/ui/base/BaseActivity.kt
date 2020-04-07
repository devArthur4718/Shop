package com.stetter.escambo.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.ui.core.CoreViewModel
import com.stetter.escambo.ui.dialog.LoadingDialog

open class BaseActivity : AppCompatActivity() {
    private val loadingDialog by lazy { LoadingDialog(this) }
    lateinit var mainViewModel: CoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProviders.of(this)[CoreViewModel::class.java]
    }

    open fun onStartLoading() {
        loadingDialog.show()
    }

    open fun onStopLoading() {
        loadingDialog.dismiss()
    }
}