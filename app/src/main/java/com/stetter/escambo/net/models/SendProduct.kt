package com.stetter.escambo.net.models

data class SendProduct (
    val uid : String = "",
    val productUrl : String = "",
    val product : String = "",
    val description : String = "",
    val category : Int = 1,
    val value : Double = 0.0
)