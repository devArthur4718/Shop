package com.stetter.escambo.extension.dialogs

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.stetter.escambo.R
import com.stetter.escambo.ui.core.add.AddProductViewModel
import com.stetter.escambo.ui.core.explore.filter.FilterViewModel
import com.stetter.escambo.ui.core.profile.UpdateProfileViewModel


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
    val dialog = CustomDialog(
        this,
        customView = view
    )
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
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()

    val close = dialog.findViewById<ImageView>(R.id.ivClosePickCam)
    close.setOnClickListener {
        dialog.dismiss()
    }

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

fun Context.showFilterValue(viewmodel : FilterViewModel){
    val dialog = ShowFilterDialog(this)
    dialog.show()

    val close = dialog.findViewById<ImageView>(R.id.tvCloseImageDialog)
    close.setOnClickListener {
        dialog.dismiss()
    }
    //Todo:  Add money mask
}

fun Context.showFilterCategory(viewmodel: FilterViewModel){
    //Todo : Pass category list from server
    val dialog = ShowCategoryDialog(this)
    dialog.show()

    val close = dialog.findViewById<ImageView>(R.id.tvCloseCategoryDialog)
    close.setOnClickListener {
        dialog.dismiss()
    }
}

fun Context.showFilterLocalization(viewmodel: FilterViewModel){

    val dialog = ShowLocalizationDialog(this)
    dialog.show()

    val close = dialog.findViewById<ImageView>(R.id.tvCloseDialogLocalization)
    close.setOnClickListener {
        dialog.dismiss()
    }
}


fun Context.checkCameraPermissions() : Boolean{
    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
        return false
    }
    return true
}

fun Context.checkGpsPermission() : Boolean{
    if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) &&  (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ) {
        return false
    }
    return true
}





