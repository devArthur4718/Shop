package com.stetter.escambo.net.retrofit.responses

import com.stetter.escambo.net.retrofit.responses.Regiao

data class UfsResponseItem(
    val id: Int,
    val nome: String,
    val regiao: Regiao,
    val sigla: String
)