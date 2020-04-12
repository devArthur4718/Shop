package com.stetter.escambo.net.models

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
    var distance: Float = 0f
) {
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
        0f
    )

}