package com.stetter.escambo.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _navigateToRegister = MutableLiveData<Boolean>()
    val navigateToRegister : LiveData<Boolean>
        get() = _navigateToRegister


    fun performRegister(){
        _navigateToRegister.value = true
    }

    fun finishedNavigatingRegister(){
        _navigateToRegister.value = false
    }
}