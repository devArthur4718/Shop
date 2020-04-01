package com.stetter.escambo.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.models.UserAddress
import com.stetter.escambo.net.retrofit.NetworkUtils
import com.stetter.escambo.net.retrofit.ViacepInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _addressValue = MutableLiveData<UserAddress>()
    val addressValue : LiveData<UserAddress> get() = _addressValue


    fun getPostalCode(cep : String){
        val retrofitClient = NetworkUtils
            .getRetrofitInstance()

        val endpoint = retrofitClient.create(ViacepInterface::class.java)
        val callback = endpoint.getAddress(cep)

        callback.enqueue(object : Callback<UserAddress> {
            override fun onResponse(call: Call<UserAddress>, response: Response<UserAddress>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(call: Call<UserAddress>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }


}