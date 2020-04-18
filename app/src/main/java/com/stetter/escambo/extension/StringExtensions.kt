package com.stetter.escambo.extension

import java.text.NumberFormat
import java.util.*

fun String.generateRandomUID() : String{
    return UUID.randomUUID().toString()
}


fun Double.toMoneyText() : String {
    var format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance("BRL")
    return format.format(this)
}