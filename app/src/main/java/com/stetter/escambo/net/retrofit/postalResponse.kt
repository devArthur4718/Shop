package com.stetter.escambo.net.retrofit

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class postalResponse : Serializable{

    @SerializedName("cep")
    val cep: String? = null

    @SerializedName("complemento")
    val complemento: String? = null

    @SerializedName("gia")
    val gia: String? = null

    @SerializedName("ibge")
    val ibge: String? = null

    @SerializedName("localidade")
    val localidade: String? = null

    @SerializedName("logradouro")
    val logradouro: String? = null

    @SerializedName("uf")
    val uf: String? = null

    @SerializedName("unidade")
    val unidade: String? = null

    @SerializedName("erro")
    val erro : Boolean = false

}
