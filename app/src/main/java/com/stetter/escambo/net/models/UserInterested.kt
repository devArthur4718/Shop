package com.stetter.escambo.net.models

import java.io.Serializable


data class UserInterested(
    var name : String = "",
    var photo : String = "",
    var uid : String = ""
) : Serializable {
    constructor() : this (
        "",
        "",
        ""
    )

}