package com.stetter.escambo.net.firebase.database

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.stetter.escambo.net.models.RegisterUser


class FirestoreRepository {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    companion object{
        const val USER_ID_FIELD = "clientID"
        const val DOCUMENT_USERS = "users"
    }

    //User collection
    fun insertUser(): CollectionReference {
        // Add a new document with a generated ID
       return db.collection("users")
    }

    fun selectUser(): DocumentReference {
        return db.collection(DOCUMENT_USERS).document(currentUserUID())
    }
    fun updateUser(): DocumentReference {
        return db.collection(DOCUMENT_USERS).document(currentUserUID())
    }

    fun updateUserPhoto(photourl : String){
        db.collection(DOCUMENT_USERS).document(currentUserUID()).update("photoURL", photourl)
    }



    //Products collection
    fun insertProduct(): CollectionReference {
        return db.collection("user-products")
    }

    fun selectUserProducts(){

    }


    fun currentUserUID() : String = auth.uid ?: ""
}