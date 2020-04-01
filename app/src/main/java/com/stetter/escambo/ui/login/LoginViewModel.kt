package com.stetter.escambo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.auth.LoginRepository
import com.stetter.escambo.net.models.Users

class LoginViewModel : ViewModel() {

    var authRepository = LoginRepository()
    var authenticatedUserLiveData: LiveData<Users>? = null

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome


    fun signInWithEmail(email: String, password: String, activity: LoginActivity) {
        authenticatedUserLiveData =
            authRepository.logInWithEmailAndPassword(email, password, activity)

        authenticatedUserLiveData?.let {
            if( it.value?.logged ?: false){
                _navigateToHome.value = true
            }else{
                _navigateToHome.value = false
            }

        }
    }

    fun logoff(): Boolean {
        authRepository.logoffUser()
        return true
    }

    fun finishedNavigateToHome() {
        _navigateToHome.value = false
    }

}