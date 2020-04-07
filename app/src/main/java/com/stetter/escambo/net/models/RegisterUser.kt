package com.stetter.escambo.net.models

data class RegisterUser (
                  var fullName : String = "",
                  var email : String = "",
                  var cep : String = "",
                  var uf : String = "",
                  var city : String = "",
                  var photoUrl : String = "",
                  var birthDate : String = "",
                  var matches : Int = 0,
                  var produtos : Int = 0,
                  var receiveNotifications : Boolean = false
)