package com.stetter.escambo.net.models

import com.stetter.escambo.ui.adapter.ProductCard

data class SendProduct(
    val uid: String = "",
    val productUrl: List<String>,
    val product: String = "",
    val description: String = "",
    val category: String = "",
    val value: Double = 0.0,
    var datePosted : Long = 0L
){
    constructor() : this("",
                            emptyList(),
                      "",
                    "",
                      "",
                        0.0,
                    0L
    )

}