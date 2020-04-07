package com.stetter.escambo.ui.register

import android.util.Log
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

class RegisterViewModel : ViewModel() {

    var authRepository = LoginRepository()
    var database = DatabaseRepository()

    private val _addressValue = MutableLiveData<postalResponse>()
    val addressValue: LiveData<postalResponse> get() = _addressValue

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean> get() = _loadingProgress

    private val _showErrorDialog = MutableLiveData<Boolean>()
    val showErrorDialog: LiveData<Boolean> get() = _showErrorDialog

    private val _registerObserver = MutableLiveData<Boolean>()
    val registerObserver : LiveData<Boolean> get() = _registerObserver


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

    fun registerUser(sendUser: RegisterUser, password : String)  {
        showLoading()
        authRepository.createUser(sendUser.email, password)
            .addOnCompleteListener {
                hideLoading()
                saveUserToDabase(sendUser,it.result?.user?.uid)
            }.addOnFailureListener {
                _registerObserver.value = false
                hideLoading()
            }
    }

    fun saveUserToDabase(sendUser: RegisterUser ,uid : String?){
        showLoading()

        database.saveUserToDabase(uid!!).setValue(sendUser)
            .addOnCompleteListener {
                hideLoading()
                _registerObserver.value = true
                Log.d("Register", "Created user $uid")
             }
            .addOnFailureListener {
                hideLoading()
                _registerObserver.value = false
                Log.e("Register", "error when creanting user $uid : $it")
            }
    }

    fun showLoading(){
        _loadingProgress.value = true
    }

    fun hideLoading(){
        _loadingProgress.value = false
    }


}