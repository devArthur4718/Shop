package com.stetter.escambo.net.retrofit

import com.stetter.escambo.net.models.UserAddress
import retrofit2.http.GET
import retrofit2.http.Path

interface ViacepInterface {

    @GET("/ws/{cep}/json/")
    fun getAddress(@Path("cep") cep: String) :retrofit2.Call<UserAddress>

}