package com.stetter.escambo.ui.core.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.auth.LoginRepository
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.net.retrofit.postalApi
import com.stetter.escambo.net.retrofit.postalResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
//                _uploadSuccess.value = true

            }
            .addOnFailureListener{
                _uploadSuccess.value = false
                _loadingProgress.value = false
            }

    }


    fun updateUser(sendUser: RegisterUser, password: String) {
        _loadingProgress.value = true
        databaserepository.updateUserToDabase().setValue(sendUser)
            .addOnCompleteListener {
                _loadingProgress.value = false
                _uploadSuccess.value = true
                //TODO: Open a new activity to update password and confirm it
//                updatePassword(password)


            }.addOnFailureListener {
                _loadingProgress.value = false
                _uploadSuccess.value = false
            }

    }

    fun updatePassword(password : String){
        _loadingProgress.value = true
        databaserepository.updatePassword(password)
            ?.addOnCompleteListener {
                _loadingProgress.value = false

            }
            ?.addOnFailureListener {
                _loadingProgress.value = false
            }

    }

    private val _showErrorDialog = MutableLiveData<Boolean>()
    val showErrorDialog: LiveData<Boolean> get() = _showErrorDialog

    private val _addressValue = MutableLiveData<postalResponse>()
    val addressValue: LiveData<postalResponse> get() = _addressValue

    fun getAddress(cep: String) {
        _loadingProgress.value = true
        postalApi.retrofitService.getPostalCodes(cep).enqueue(object : Callback<postalResponse> {
            override fun onFailure(call: Call<postalResponse>, t: Throwable) {
                _loadingProgress.value = false
                _showErrorDialog.value = true
            }

            override fun onResponse(
                call: Call<postalResponse>,
                response: Response<postalResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.erro?.let {
                        if (it) {
                            _loadingProgress.value = false
                            _showErrorDialog.value = true

                        } else {
                            _addressValue.value = response.body()
                            _loadingProgress.value = false
                        }
                    }
                }

            }
        })
    }

}