package com.stetter.escambo.ui.splash

import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.auth.LoginRepository

class SplashViewModel : ViewModel(){

    var authRepository = LoginRepository()

    fun isUserLogged() : Boolean = authRepository.isUserLogged()
}