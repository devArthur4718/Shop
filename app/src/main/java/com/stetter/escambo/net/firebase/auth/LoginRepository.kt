package com.stetter.escambo.net.firebase.auth

import android.util.Log
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

class LoginRepository {

    var auth = FirebaseAuth.getInstance()

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
        auth.signOut()
        return true
    }

    fun isUserLogged() : Boolean =  if(auth.currentUser != null) true else false

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
                Log.d("createUser", "Fail: $it")
            }
    }

    fun getCurrentUserUID() : String = auth.uid ?: ""


}