package com.stetter.escambo.extension

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.stetter.escambo.R
import com.stetter.escambo.ui.core.add.AddProductViewModel
import com.stetter.escambo.ui.core.profile.UpdateProfileViewModel
import java.util.jar.Manifest


fun Context.showDialog(
    title: String,
    message: String,
    yes: String,
    no: String?,
    confirmListener: ((CustomDialog) -> Any)?,
    cancelListener: ((CustomDialog) -> Any)?,
    canceledOnTouchTouchOutside: Boolean? = false,
    view: View? = null
) {
    val dialog = CustomDialog(this, customView = view)
    dialog.title = title
    dialog.message = message
    dialog.confirmText = yes
    dialog.cancelText = no

    dialog.setCanceledOnTouchOutside(!canceledOnTouchTouchOutside!!)

    confirmListener?.run {
        dialog.setOnConfirmButtonClickListener {
            confirmListener(it)
            it.dismiss()
        }
    }
    cancelListener?.run {
        dialog.setOnCancelButtonClickListener {
            cancelListener(it)
            it.dismiss()
        }
    }

    dialog.show()
}

fun Context.showPickImageDialog(viewModel: AddProductViewModel) {

    val dialog = ShowCameraDialog(this)
    dialog.show()

    var btnPick = dialog.findViewById<Button>(R.id.tvPickFromGallery)
    var btnPhoto = dialog.findViewById<Button>(R.id.tvPickFromCamera)
    btnPick.setOnClickListener {
        viewModel.openPickPhotoIntent()
        dialog.dismiss()
    }
    btnPhoto.setOnClickListener {
        viewModel.openCameraIntent()
        dialog.dismiss()
    }
}

fun Context.showPickImageProfile(viewModel : UpdateProfileViewModel){

    val dialog = ShowCameraDialog(this)
    dialog.show()

    var btnPick = dialog.findViewById<Button>(R.id.tvPickFromGallery)
    var btnPhoto = dialog.findViewById<Button>(R.id.tvPickFromCamera)

    btnPick.setOnClickListener {
        viewModel.openPickPhotoIntent()
        dialog.dismiss()
    }

    btnPhoto.setOnClickListener {
//        viewModel.openCameraIntent()
        dialog.dismiss()
    }
}

fun Context.showFilterValue(){


}


fun Context.checkCameraPermissions() : Boolean{

    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
        return false
    }
    return true
}


