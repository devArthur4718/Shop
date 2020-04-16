package com.stetter.escambo.extension

import java.util.*

fun String.generateRandomUID() : String{
    return UUID.randomUUID().toString()
}