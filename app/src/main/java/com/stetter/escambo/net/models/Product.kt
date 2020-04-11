package com.stetter.escambo.net.models


data class Product(
    val uid: String = "",
    val productUrl: List<String>,
    val product: String = "",
    val description: String = "",
    val category: String = "",
    val value: Double = 0.0,
    var datePosted : Long = 0L,
    val username : String = ""
){
    constructor() : this("",
                            emptyList(),
                      "",
                    "",
                      "",
                        0.0,
                    0L,
                    ""
    )

}