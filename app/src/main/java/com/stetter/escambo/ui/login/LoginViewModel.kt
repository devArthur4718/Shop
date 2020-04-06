package com.stetter.escambo.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.stetter.escambo.net.firebase.auth.LoginRepository
import com.stetter.escambo.net.models.Users
import com.stetter.escambo.ui.recovery.RecoveryPassword

class LoginViewModel : ViewModel() {

    var authRepository = LoginRepository()

    private val _userUID = MutableLiveData<String>()
    val userUID: LiveData<String> get() = _userUID

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean> get() = _loadingProgress

    private val _loginError = MutableLiveData<String>()
    val loginError : LiveData<String> get() = _loginError

    fun signInWithEmail(email: String, password: String) {
        authRepository.logInWithEmailAndPassword(email, password)
            .addOnCompleteListener {result ->

                if(result.isSuccessful){
                    result.result?.user?.uid?.let {UID ->
                        hideLoading()
                        sendUserUID(UID)
                    }
                }else{

                    hideLoading()
                    loginError("Usuário ou/senha incorretos")
                }

            }
            .addOnFailureListener {
                hideLoading()
            }
    }
    fun signInWithFacebookCredential(token : AccessToken){
        authRepository.logInWithFacebookToken(token)
            .addOnCompleteListener {task->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d("facebook", "signInWithCredential:success")
                    val user =   authRepository.auth.currentUser
                    hideLoading()
                    user?.let { it -> sendUserUID(it.uid)  }

                } else {
                    // If sign in fails, display a message to the user.
                    hideLoading()
                    loginError("Erro ao efetuar login")
                }
            }
            .addOnFailureListener {
                hideLoading()
            }
    }
    fun signInWithGoogleCredential(account: GoogleSignInAccount) {
        authRepository.logWithGoogleToken(account)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Log.d("facebook", "signWithGoogle:success")
                    val user =   authRepository.auth.currentUser
                    hideLoading()
                    user?.let { it -> sendUserUID(it.uid)  }
                }
                else {
                    // If sign in fails, display a message to the user.
                    hideLoading()
                    loginError("Erro ao efetuar login")

                }

            }
            .addOnFailureListener {
                hideLoading()
            }
    }

    private fun loginError(msg :String) {
        _loginError.value = msg
    }

    fun sendUserUID(UID : String){
        _userUID.value = UID
    }

    fun logoff(): Boolean {
        authRepository.logoffUser()
        return true
    }

    fun recoveryPassword(email : String) : Boolean?{
        return authRepository.recoverPassword(email)
    }

    fun showLoading(){
        _loadingProgress.value = true
    }

    fun hideLoading(){
        _loadingProgress.value = false
    }



}