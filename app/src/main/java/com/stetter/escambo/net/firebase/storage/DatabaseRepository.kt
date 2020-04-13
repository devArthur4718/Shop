package com.stetter.escambo.net.firebase.storage

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.storage.BuildConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class DatabaseRepository {

    var auth = FirebaseAuth.getInstance()

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

    fun updateProductCount(count : Int): Task<Void> {
        var uid = getCurrentUserUID()
        return FirebaseDatabase.getInstance().getReference("/users/$uid").child("products").setValue(count)
    }

    fun updateUserToDabase(): DatabaseReference {
        var uid = getCurrentUserUID()
        return FirebaseDatabase.getInstance().getReference("/users").child(uid)
    }

    fun updatePassword(password : String): Task<Void>? {
        val user = auth.currentUser
        return user?.updatePassword(password)
    }


    fun retriveUserData() : DatabaseReference{
        var uid = getCurrentUserUID()
        return FirebaseDatabase.getInstance().getReference("/users/$uid")
    }

    fun retrieveAnotherUser(uid : String) : DatabaseReference{
        return FirebaseDatabase.getInstance().getReference("/users/$uid")
    }

    fun getCurrentUserUID() : String = auth.uid ?: ""

    fun retriveUserProducts(): Query {
        return FirebaseDatabase.getInstance().getReference("/products").orderByChild("uid").equalTo(getCurrentUserUID())
    }

    fun retriveUserProductsByName(username : String): Query {
        return FirebaseDatabase.getInstance().getReference("/products").orderByChild("username").equalTo(username)
    }

    fun retrieveRecentPosts() : Query {
        //Firebase always order um asc order, in order to return desc order we need to reverse the list in the UI
        return  FirebaseDatabase.getInstance().getReference("/products").orderByChild("datePosted")
    }

    fun receiveTopUsers() : Query{
        //Firebase always order um asc order, in order to return desc order we need to reverse the list in the UI
        return FirebaseDatabase.getInstance().getReference("/users").orderByChild("matches")
    }

    fun receiveProductsCloseToMe(): Query {
        return  FirebaseDatabase.getInstance().getReference("/products")
    }
}