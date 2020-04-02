package com.stetter.escambo.net.firebase.auth

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.stetter.escambo.net.models.Users
import com.stetter.escambo.ui.dialog.LoadingDialog
import com.stetter.escambo.ui.login.LoginActivity
import com.stetter.escambo.ui.recovery.RecoveryPassword

class LoginRepository {

    var auth = FirebaseAuth.getInstance()
    var userLiveData : MutableLiveData<Users> = MutableLiveData()
    private lateinit var loadingDialog : LoadingDialog




    fun logInWithEmailAndPassword(email : String = "email", password : String = "password", activity: LoginActivity): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email,password)
    }
    fun logInWithFacebookToken(token : AccessToken): Task<AuthResult> {
        val crendtial = FacebookAuthProvider.getCredential(token.token)
        return auth.signInWithCredential(crendtial)
    }

    fun logoffUser() : Boolean{
        userLiveData.value = null
        auth.signOut()
        return true
    }

    var recoverStatus : Boolean? = null
    fun recoverPassword(email : String, activity: RecoveryPassword): Boolean? {
        loadingDialog = LoadingDialog(activity)
        loadingDialog.show()
        auth.sendPasswordResetEmail(email).addOnCompleteListener {task ->
            if(task.isSuccessful){
                recoverStatus = true
                loadingDialog.hide()
                return@addOnCompleteListener
            }else{
                recoverStatus = false
                loadingDialog.hide()
                return@addOnCompleteListener
            }

        }.addOnFailureListener {
            Toast.makeText(activity,"Error: ${it}", Toast.LENGTH_SHORT).show()
            recoverStatus = false
            loadingDialog.hide()
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