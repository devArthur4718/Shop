package com.stetter.escambo.extension

object StringUtil {
    fun isNullOrEmpty(param: String?): Boolean {
        return param == null || param.isEmpty()
    }
}
