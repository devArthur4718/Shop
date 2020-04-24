package com.stetter.escambo.net.models

import java.io.Serializable

data class ProductInterest(
    var productName : String = "",
    var productPhoto : String = "",
    var productKey : String = "",
    var ownerUID : String = ""
) : Serializable {
    constructor() : this (
        "",
        "",
        "",
        ""
    )

}