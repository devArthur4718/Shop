package com.stetter.escambo.net.firebase.auth

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.stetter.escambo.net.models.Users
import com.stetter.escambo.ui.dialog.LoadingDialog
import com.stetter.escambo.ui.login.LoginActivity
import com.stetter.escambo.ui.recovery.RecoveryPassword

class LoginRepository {

    var auth = FirebaseAuth.getInstance()
    var userLiveData : MutableLiveData<Users> = MutableLiveData()

    fun logInWithEmailAndPassword(email : String = "email", password : String = "password"): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email,password)
    }

    fun logInWithFacebookToken(token : AccessToken): Task<AuthResult> {
        val credential = FacebookAuthProvider.getCredential(token.token)
        return auth.signInWithCredential(credential)
    }

    fun logWithGoogleToken(completedTask :GoogleSignInAccount): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(completedTask.idToken, null)
        return auth.signInWithCredential(credential)
    }

    fun logoffUser() : Boolean{
        userLiveData.value = null
        auth.signOut()
        return true
    }

    var recoverStatus : Boolean? = null
    fun recoverPassword(email : String): Boolean? {


        auth.sendPasswordResetEmail(email).addOnCompleteListener {task ->
            if(task.isSuccessful){
                recoverStatus = true
                return@addOnCompleteListener
            }else{
                recoverStatus = false
                return@addOnCompleteListener
            }

        }.addOnFailureListener {
            recoverStatus = false
            return@addOnFailureListener
        }

        return recoverStatus
    }

    fun createUser( email : String,  password : String): Task<AuthResult> {
       return auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d("createUser", "Successs")

             }.addOnFailureListener {
                Log.d("createUser", "Fail")
            }
    }
}