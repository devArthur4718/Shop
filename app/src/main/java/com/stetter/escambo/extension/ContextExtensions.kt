package com.stetter.escambo.extension

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import java.util.*


fun Context.showKeyboard() {
    val imm: InputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Context.hideKeyBoard(view : View){
    val imm =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
}

fun Calendar.getTimeStamp(): Long {
       return this.timeInMillis
}

fun Context.GeocoderLocation(location : String) : Pair<Double,Double>{
    var latitude  = 0.0
    var longitute  = 0.0
    if (Geocoder.isPresent()) {
        var gc = Geocoder(this)
        var address = gc.getFromLocationName(location, 5)
        address.forEach {
            address.forEach {
                if (it.hasLatitude() && it.hasLongitude()) {
                     latitude = it.latitude
                     longitute = it.longitude
                }
            }
        }
    }
    return Pair(latitude,longitute)
}


fun Float.metersToKM(): Long = Math.round(this/1000.0)

fun Context.isGPsEnabled() : Boolean{
    var lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
}
//
//fun Context.hasGpsPermission() : Boolean{
//    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && )
//}
