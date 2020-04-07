package com.stetter.escambo.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle


class CustomDialog(var msg : String, context: Context) : AlertDialog(context)
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alertDialog: AlertDialog? = context?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                 "OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                    })
            }
            builder.create()
        }
    }
}