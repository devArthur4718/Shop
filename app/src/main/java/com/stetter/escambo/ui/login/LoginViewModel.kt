package com.stetter.escambo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.auth.LoginRepository
import com.stetter.escambo.net.models.Users
import com.stetter.escambo.ui.recovery.RecoveryPassword

class LoginViewModel : ViewModel() {

    var authRepository = LoginRepository()
    var authenticatedUserLiveData: LiveData<Users>? = null

    private val _userUID = MutableLiveData<String>()
    val userUID: LiveData<String> get() = _userUID

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean> get() = _loadingProgress

    fun signInWithEmail(email: String, password: String, activity: LoginActivity) {
        authRepository.logInWithEmailAndPassword(email, password, activity)
            .addOnCompleteListener {result ->
                result.result?.user?.uid?.let {UID ->
                    sendUserUID(UID)
                    hideLoading()
                }
            }
            .addOnFailureListener {
                hideLoading()
                //TODO: Iinform user
            }
    }

    fun sendUserUID(UID : String){
        _userUID.value = UID
    }

    fun logoff(): Boolean {
        authRepository.logoffUser()
        return true
    }

    fun recoveryPassword(email : String, activity : RecoveryPassword) : Boolean?{
        return authRepository.recoverPassword(email, activity)
    }

    fun showLoading(){
        _loadingProgress.value = true
    }

    fun hideLoading(){
        _loadingProgress.value = false
    }


}