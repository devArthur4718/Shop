package com.stetter.escambo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.auth.LoginRepository
import com.stetter.escambo.net.models.Users

class LoginViewModel : ViewModel() {

    var authRepository = LoginRepository()
    var authenticatedUserLiveData: LiveData<Users>? = null

    fun signInWithEmail(email : String , password : String, activity : LoginActivity){
        authenticatedUserLiveData = authRepository.logInWithEmailAndPassword(email,password, activity)
    }

}