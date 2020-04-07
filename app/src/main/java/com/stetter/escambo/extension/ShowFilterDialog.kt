package com.stetter.escambo.extension

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.stetter.escambo.R

class ShowFilterDialog(context : Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            setContentView(R.layout.dialog_filter)
        }
    }
}