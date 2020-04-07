package com.stetter.escambo.ui.core.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.auth.LoginRepository
import com.stetter.escambo.net.firebase.storage.DatabaseRepository

class UpdateProfileViewModel  : ViewModel(){


    var authRepository = LoginRepository()
    val databaserepository = DatabaseRepository()


    private val _imagePickIntent = MutableLiveData<Boolean>()
    val imagePickIntent : LiveData<Boolean> get() = _imagePickIntent

    private val _pickFromGalleryObservable = MutableLiveData<Boolean>()
    val pickPhotoFromGallery : LiveData<Boolean> get() = _pickFromGalleryObservable

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress : LiveData<Boolean> get() = _loadingProgress

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSucess : LiveData<Boolean> get() = _uploadSuccess

    private val _onPhotoFileReceived = MutableLiveData<String>()
    val onPhotoFileReceived : LiveData<String> get() = _onPhotoFileReceived

    fun logout(){

        authRepository.logoffUser()
    }

    fun openPickPhotoIntent() {
        _imagePickIntent.value = true
    }
    fun closePhotoIntent(){
        _imagePickIntent.value = false
    }

    fun pickPhoto(){
        _pickFromGalleryObservable.value = true
    }

    fun onPickPhotoSuccess(){
        _pickFromGalleryObservable.value = false
    }

    fun uploadImageToFirebase(filename : String, byteArray : ByteArray) {
        _loadingProgress.value = true
        databaserepository.uploadImageToDatabase(filename).putBytes(byteArray)
            .addOnSuccessListener {
                //Todo : use url do download image inside circle image view
                _onPhotoFileReceived.value = it.metadata?.path
                _loadingProgress.value = false
                _uploadSuccess.value = true

            }
            .addOnFailureListener{
                _uploadSuccess.value = false
                _loadingProgress.value = false
            }

    }

    fun uploadImageToFirebase(filename : String, uri : Uri) {
        _loadingProgress.value = true
        databaserepository.uploadImageToDatabase(filename).putFile(uri)
            .addOnSuccessListener {
                _onPhotoFileReceived.value = it.metadata?.path
                _loadingProgress.value = false
                _uploadSuccess.value = true

            }
            .addOnFailureListener{
                _uploadSuccess.value = false
                _loadingProgress.value = false
            }

    }

}