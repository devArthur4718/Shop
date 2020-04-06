package com.stetter.escambo.net.firebase.storage

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.BuildConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class DatabaseRepository {

    fun uploadImageToDatabase(filename: String): StorageReference {
       return FirebaseStorage.getInstance().getReference(if(BuildConfig.DEBUG) "/images/$filename" else "debugimages/$filename")
    }


    fun updateProductToDabatase(uid : String): DatabaseReference {
        var productUID = UUID.randomUUID().toString()
        return FirebaseDatabase.getInstance().getReference("/products/$productUID")
    }

    fun getUID() : String{
        return FirebaseAuth.getInstance().uid ?: ""
    }

}