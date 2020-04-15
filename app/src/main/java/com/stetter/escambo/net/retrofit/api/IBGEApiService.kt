package com.stetter.escambo.net.retrofit.api

import com.stetter.escambo.net.retrofit.responses.CityResponse
import com.stetter.escambo.net.retrofit.responses.CityResponseItem
import com.stetter.escambo.net.retrofit.responses.UfsResponseItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


private const val BASE_URL =
    "https://servicodados.ibge.gov.br/api/v1/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface IBGEApiService{
    @GET("localidades/estados")
    fun getUfsIds() : Call<ArrayList<UfsResponseItem>>

    @GET("localidades/estados/{id}/municipios")
    fun getCities(@Path("id") id : Int ) : Call<ArrayList<CityResponseItem>>

}

object IBGEapi{
    val IBGESservice : IBGEApiService by lazy {
        retrofit.create(IBGEApiService::class.java)
    }
}