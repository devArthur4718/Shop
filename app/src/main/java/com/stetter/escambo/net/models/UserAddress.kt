package com.stetter.escambo.net.models

import com.google.gson.annotations.SerializedName

data class UserAddress(
    @SerializedName("cep")
    val cep: String,
    @SerializedName("localidade")
    val localidade: String,
    @SerializedName("uf")
    val uf: String
)