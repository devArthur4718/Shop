package com.stetter.escambo.net.firebase.storage

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.storage.BuildConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stetter.escambo.net.models.RegisterUser
import java.util.*

class DatabaseRepository {

    fun uploadImageToDatabase(filename: String): StorageReference {
       return FirebaseStorage.getInstance().getReference(if(BuildConfig.DEBUG) "/images/$filename" else "debugimages/$filename")
    }

    fun updateProductToDabatase(uid : String): DatabaseReference {
        var productUID = UUID.randomUUID().toString()
        return FirebaseDatabase.getInstance().getReference("/products/$productUID")
    }

    fun saveUserToDabase(uid : String): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("/users/$uid")
    }

    fun retriveUserProducts(): Query {
       return FirebaseDatabase.getInstance().getReference("/products").orderByChild("uid").equalTo("J3jxIFsPlLgnHllfb2HBzqXtNTT2")
    }

    fun retriveUserData() : DatabaseReference{
        var uid = getUID()
        return FirebaseDatabase.getInstance().getReference("/users/$uid")
    }

    fun getUID() : String{
        return FirebaseAuth.getInstance().uid ?: ""
    }

}