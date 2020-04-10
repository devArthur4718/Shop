package com.stetter.escambo.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
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