package com.stetter.escambo.net.retrofit.responses

data class Microrregiao(
    val id: Int,
    val mesorregiao: Mesorregiao,
    val nome: String
)