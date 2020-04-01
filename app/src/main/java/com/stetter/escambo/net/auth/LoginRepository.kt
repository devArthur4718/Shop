package com.stetter.escambo.net.auth

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.stetter.escambo.net.models.Users
import com.stetter.escambo.ui.login.LoginActivity

class LoginRepository {

    var auth = FirebaseAuth.getInstance()
    var userLiveData : MutableLiveData<Users> = MutableLiveData()
    var loggedUser = Users()

    fun logInWithEmailAndPassword(email : String = "", password : String = "", activity: LoginActivity) : MutableLiveData<Users> {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener((activity)) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    var firebaseUser = auth.currentUser
                    if(firebaseUser != null){

                        loggedUser.uid = firebaseUser.uid
                        loggedUser.email = firebaseUser.email.toString()
                        loggedUser.name = firebaseUser.displayName.toString()
                        userLiveData.value = loggedUser

                    }

                    Toast.makeText(activity, "Auth Success: UID ${loggedUser.uid}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()                }
            }

        return userLiveData

    }
}