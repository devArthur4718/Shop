package com.stetter.escambo.extension

import android.util.Patterns
import android.widget.EditText


fun EditText.clearError() {
    error = null
}

fun EditText.isEmailValid() : Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(this.text).matches()
}

fun EditText.isFullNameValid() : Boolean {
    return if (this.text.length > 8) true else false
}

fun EditText.isNullOrEmpty() : Boolean {
    return this.text.isNullOrEmpty()
}

fun EditText.isPasswordValid() : Boolean {
    return if (this.text.length > 8) true else false
}