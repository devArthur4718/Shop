package com.stetter.escambo.net.firebase.storage

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.storage.BuildConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.stetter.escambo.net.models.RegisterUser
import java.util.*

class DatabaseRepository {

    var auth = FirebaseAuth.getInstance()

    fun uploadImageToDatabase( byteArray: ByteArray): StorageReference {
       return FirebaseStorage.getInstance().getReference(if(BuildConfig.DEBUG) "/images/$byteArray" else "debugimages/$byteArray")
    }

    fun uploadImageToDatabase( filename : String): StorageReference {
        return FirebaseStorage.getInstance().getReference(if(BuildConfig.DEBUG) "/images/$filename" else "debugimages/$filename")
    }


    fun updateProductToDabatase(): DatabaseReference {
        var productUID = UUID.randomUUID().toString()
        return FirebaseDatabase.getInstance().getReference("/products/$productUID")
    }

    fun saveUserToDabase(uid : String): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("/users/$uid")
    }

    fun updateUserToDabase(): DatabaseReference {
        var uid = getCurrentUserUID()
        return FirebaseDatabase.getInstance().getReference("/users").child(uid)
    }

    fun updateName(username : String): Task<Void>? {
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()
        return user?.updateProfile(profileUpdates)
    }

    fun updatePassword(password : String): Task<Void>? {
        val user = auth.currentUser
        return user?.updatePassword(password)
    }

    fun retriveUserProducts(): Query {
       return FirebaseDatabase.getInstance().getReference("/products").orderByChild("uid").equalTo(getCurrentUserUID())
    }

    fun retriveUserData() : DatabaseReference{
        var uid = getCurrentUserUID()
        return FirebaseDatabase.getInstance().getReference("/users/$uid")
    }

    fun getCurrentUserUID() : String = auth.uid ?: ""

    fun retrieveRecentPosts() : Query {
        //Dont have to order by time stamp, the dabase already is ordening each one
        return  FirebaseDatabase.getInstance().getReference("/products").orderByChild("datePosted")
    }


}