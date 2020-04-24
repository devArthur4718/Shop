package com.stetter.escambo.net.models

import java.io.Serializable

data class Product(
    var uid: String = "",
    var productUrl: List<String>,
    var product: String = "",
    var description: String = "",
    var category: String = "",
    var value: String = "",
    var datePosted : Long = 0L,
    var username : String = "",
    var userPhoto : String = "",
    var lat : Double = 0.0,
    var lng : Double = 0.0,
    var uf : String = "",
    var city : String = "",
    var productKey : String = ""
) : Serializable{
    constructor() : this("",
                            emptyList(),
                      "",
                    "",
                      "",
                        "",
                    0L,
                    "",
                    "",
                    0.0,
                    0.0,
                    "",
                    "",
            ""
    )

}