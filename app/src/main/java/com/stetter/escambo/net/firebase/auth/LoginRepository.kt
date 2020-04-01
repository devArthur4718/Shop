package com.stetter.escambo.net.firebase.auth

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.stetter.escambo.net.models.Users
import com.stetter.escambo.ui.dialog.LoadingDialog
import com.stetter.escambo.ui.login.LoginActivity
import com.stetter.escambo.ui.recovery.RecoveryPassword

class LoginRepository {

    var auth = FirebaseAuth.getInstance()
    var userLiveData : MutableLiveData<Users> = MutableLiveData()
    private lateinit var loadingDialog : LoadingDialog
    var loggedUser = Users()

    fun logInWithEmailAndPassword(email : String = "email", password : String = "password", activity: LoginActivity) : MutableLiveData<Users> {

        loadingDialog = LoadingDialog(activity)
        loadingDialog.show()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((activity)) { task ->
                    if (task.isSuccessful) {
                        var firebaseUser = auth.currentUser
                        if(firebaseUser != null){
                            loggedUser.uid = firebaseUser.uid
                            loggedUser.email = firebaseUser.email.toString()
                            loggedUser.name = firebaseUser.displayName.toString()
                            loggedUser.logged = true
                            userLiveData.value = loggedUser
                            loadingDialog.hide()
                        }

                    } else {
                        loggedUser.uid = ""
                        loggedUser.email = ""
                        loggedUser.name = ""
                        loggedUser.logged = false
                        loadingDialog.hide()
                        Toast.makeText(activity, "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT).show()                }
                }.addOnFailureListener {
                    loggedUser.uid = ""
                    loggedUser.email = ""
                    loggedUser.name = ""
                    loggedUser.logged = false
                    loadingDialog.hide()
                    Toast.makeText(activity, "Authentication failed: ${it}",
                        Toast.LENGTH_SHORT).show()

                }

        return userLiveData

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
}