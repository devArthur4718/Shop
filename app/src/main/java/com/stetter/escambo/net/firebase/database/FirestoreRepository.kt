package com.stetter.escambo.net.firebase.database

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class FirestoreRepository {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    //User collection
    fun insertUser(): CollectionReference {
        // Add a new document with a generated ID
       return db.collection("users")
    }

    fun selectUser(): Task<QuerySnapshot> {
        return db.collection("users").whereEqualTo("clientID", currentUserUID()).get()
    }

    //Products collection
    fun insertProduct(): CollectionReference {
        return db.collection("user-products")
    }

    fun selectUserProducts(){

    }


    fun currentUserUID() : String = auth.uid ?: ""
}