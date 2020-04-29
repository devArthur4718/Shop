package com.stetter.escambo.ui.base

import android.app.AlertDialog
import android.content.DialogInterface
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

    private fun showErrorDialog(text: String): AlertDialog {
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
                setNegativeButton(
                    "Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })

                setMessage(text)
            }
            builder.create()
        }

        alertDialog?.show()

        return alertDialog!!
    }


    open fun onStartLoading() {
        loadingDialog.show()
    }

    open fun onStopLoading() {
        loadingDialog.dismiss()
    }
}