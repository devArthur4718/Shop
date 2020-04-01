package com.stetter.escambo.net.models

data class Users (var uid : String = "",
                  var name : String = "",
                  var email : String = ""
){

    constructor() : this("", "", "" )



}