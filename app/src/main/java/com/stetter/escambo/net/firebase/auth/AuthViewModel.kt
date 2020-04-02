package com.stetter.escambo.net.firebase.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.models.Users
import com.stetter.escambo.ui.login.LoginActivity


class AuthViewModel : ViewModel(){

    var authRepository = LoginRepository()
    var authenticatedUserLiveData: LiveData<Users>? = null


}