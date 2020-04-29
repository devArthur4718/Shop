package com.stetter.escambo.net.models

import java.io.Serializable

data class ProductInterest(
    var productName : String = "",
    var productPhoto : String = "",
    var productKey : String = "",
    var ownerUID : String = "",
    var interestUID : String = "",
    var interestUserName : String = "",
    var interestPhoto : String = ""
) : Serializable {
    constructor() : this (
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )

}