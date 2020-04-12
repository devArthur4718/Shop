package com.stetter.escambo.net.models

import java.io.Serializable

data class ProductByLocation(
    val uid: String = "",
    val productUrl: List<String>,
    val product: String = "",
    val description: String = "",
    val category: String = "",
    val value: Double = 0.0,
    var datePosted: Long = 0L,
    val username: String = "",
    var userPhoto: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var distance: Float = 0f,
    var uf : String = "",
    var city : String = ""
) : Serializable {
    constructor() : this(
        "",
        emptyList(),
        "",
        "",
        "",
        0.0,
        0L,
        "",
        "",
        0.0,
        0.0,
        0f,
        "",
        ""
    )

}