package com.stetter.escambo.net.models

import java.io.Serializable

data class Product(
    val uid: String = "",
    val productUrl: List<String>,
    val product: String = "",
    val description: String = "",
    val category: String = "",
    val value: String = "",
    val datePosted : Long = 0L,
    val username : String = "",
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