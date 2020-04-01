package com.stetter.escambo.net.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.models.Users
import com.stetter.escambo.ui.login.LoginActivity


class AuthViewModel : ViewModel(){

    var authRepository = LoginRepository()
    var authenticatedUserLiveData: LiveData<Users>? = null

    fun signInWithEmail(email : String , password : String, activity : LoginActivity){
        authenticatedUserLiveData = authRepository.logInWithEmailAndPassword(email,password, activity)
    }

}