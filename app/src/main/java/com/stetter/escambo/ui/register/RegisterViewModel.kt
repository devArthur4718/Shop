package com.stetter.escambo.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.retrofit.postalApi
import com.stetter.escambo.net.retrofit.postalResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _addressValue = MutableLiveData<postalResponse>()
    val addressValue : LiveData<postalResponse> get() = _addressValue

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress : LiveData<Boolean> get() = _loadingProgress

    private val _showErrorDialog = MutableLiveData<Boolean>()
    val showErrorDialog : LiveData<Boolean> get() = _showErrorDialog

    //form
    val fullName = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")

    fun getAddress(cep : String){
        _loadingProgress.value =  true
        postalApi.retrofitService.getPostalCodes(cep).enqueue(object : Callback<postalResponse>{
            override fun onFailure(call: Call<postalResponse>, t: Throwable) {
                _loadingProgress.value = false
                _showErrorDialog.value = true
            }
            override fun onResponse(
                call: Call<postalResponse>,
                response: Response<postalResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.erro?.let {
                        if(it){
                            _loadingProgress.value = false
                            _showErrorDialog.value = true

                        }else{
                            _addressValue.value = response.body()
                            _loadingProgress.value = false
                        }
                    }
                }

            }
        })
    }

    fun dismissDialog(){
        _showErrorDialog.value = false
    }



}