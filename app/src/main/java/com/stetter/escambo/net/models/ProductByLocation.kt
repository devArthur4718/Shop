package com.stetter.escambo.net.models

import android.location.Location
import com.stetter.escambo.extension.metersToKM
import java.io.Serializable

data class ProductByLocation(
    val uid: String = "",
    val productUrl: List<String>,
    val product: String = "",
    val description: String = "",
    val category: String = "",
    val value: String = "",
    var datePosted: Long = 0L,
    val username: String = "",
    var userPhoto: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var distance: Float = 0f,
    var uf : String = "",
    var city : String = "",
    var productKey : String = ""
) : Serializable {
    constructor() : this(
        "",
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
        0f,
        "",
        "",
        ""
    )

    fun distanceBetween(lat1: Double?, lng1: Double?, lat2: Double?, lng2: Double?) : Float {
        var locationA = Location("PointA")
        lat1?.let { locationA.latitude = it }
        lng1?.let { locationA.longitude = it }
        var locationB = Location("PointB")
        lat2?.let { locationB.latitude = lat2 }
        lng2?.let { locationB.longitude = lng2 }
        var distance = locationA.distanceTo(locationB)
        return distance.metersToKM().toFloat()
    }

}