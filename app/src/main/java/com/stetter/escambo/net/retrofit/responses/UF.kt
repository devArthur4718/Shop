package com.stetter.escambo.net.retrofit.responses

data class UF(
    val id: Int,
    val nome: String,
    val regiao: RegiaoX,
    val sigla: String
)