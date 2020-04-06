package com.stetter.escambo.extension

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.stetter.escambo.R
import kotlinx.android.synthetic.main.dialog_photo_gallery.*

class CustomDialog(context: Context, private var customView: View? = null) : Dialog(context) {

    var title: String? = null
    var message: String? = null
    var confirmText: String? = null
    var cancelText: String? = null
    var confirmButtonListener: (CustomDialog) -> Any = { it.dismiss() }
    var cancelListener: (CustomDialog) -> Any = { it.dismiss() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customView?.run {
            setContentView(this)
            val params = window?.attributes
            params?.width = WindowManager.LayoutParams.WRAP_CONTENT
            params?.height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.attributes = params
        } ?: kotlin.run { setContentView(R.layout.dialog_photo_gallery) }
        if (window != null) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        if (!StringUtil.isNullOrEmpty(title)) tvDialogTittle?.text = title
        if (!StringUtil.isNullOrEmpty(confirmText)) tvPickFromCamera?.text = confirmText
        if (!StringUtil.isNullOrEmpty(cancelText)) tvPickFromGallery?.text = cancelText

        ivCloseDialog.setOnClickListener {

        }

    }


    fun showCancelButton(show: Boolean): CustomDialog {
        return this
    }

    fun setOnConfirmButtonClickListener(listener: (CustomDialog) -> Any): CustomDialog {
        confirmButtonListener = listener
        return this
    }

    fun setOnCancelButtonClickListener(listener: (CustomDialog) -> Any): CustomDialog {
        this.cancelListener = listener
        return this
    }


}