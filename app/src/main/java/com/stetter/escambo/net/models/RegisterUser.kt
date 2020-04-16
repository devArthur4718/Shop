package com.stetter.escambo.net.models

import java.io.Serializable

data class RegisterUser (
    var fullName : String = "",
    var email : String = "",
    var cep : String = "",
    var uf : String = "",
    var city : String = "",
    var photoUrl : String = "",
    var birthDate : String = "",
    var matches : Int = 0,
    var products : Int = 0,
    var receiveNotifications : Boolean = false,
    var lat : Double = 0.0,
    var lng : Double = 0.0,
    var clientID : String = "",
    var productsList : List<String>

) : Serializable{
    constructor( ) : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        0,
        false,
        0.0,
        0.0,
        "",
        emptyList()

    )
}