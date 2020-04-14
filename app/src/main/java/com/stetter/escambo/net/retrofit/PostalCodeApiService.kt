package com.stetter.escambo.net.retrofit

import com.stetter.escambo.net.retrofit.responses.postalResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


private const val BASE_URL =
    "https://viacep.com.br/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface PostalCodeApiService{
    @GET("ws/{cep}/json/")
    fun getPostalCodes(@Path("cep") cep : String) : Call<postalResponse>
}

object postalApi{
    val retrofitService : PostalCodeApiService by lazy {
        retrofit.create(PostalCodeApiService::class.java)
    }
}