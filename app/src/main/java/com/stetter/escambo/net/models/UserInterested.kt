package com.stetter.escambo.net.models

import java.io.Serializable


data class UserInterested(
    var name : String = "teste",
    var photo : String = "testephoto",
    var uid : String = "testeuid"
) : Serializable {
    constructor() : this (
        "testeName",
        "testePhoto",
        "testeUID"
    )

}