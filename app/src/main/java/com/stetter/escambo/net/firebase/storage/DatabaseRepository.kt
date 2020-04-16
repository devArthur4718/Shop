package com.stetter.escambo.net.firebase.storage

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DatabaseRepository {

    var auth = FirebaseAuth.getInstance()

    companion object{
        const val PATH_PRODUCTS = "/products"
        const val PATH_USERS = "/users"
        const val PATH_CATEGORIES = "/categories"
    }

    fun uploadImageToDatabase( filename : String): StorageReference {
        return FirebaseStorage.getInstance().getReference(  "debugimages/$filename")
    }

    fun updateProductToDabatase(productUID : String): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("/products/$productUID")
    }

    fun saveUserToDabase(uid : String): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("/users/$uid")
    }

    fun updateProductCount(count : Int): Task<Void> {
        return FirebaseDatabase.getInstance().getReference("/users/${currentUserUID()}").child("products").setValue(count)
    }

    fun updateUserToDabase(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(PATH_USERS).child(currentUserUID())
    }

    fun updateUserProductList(productUID : ArrayList<String>) : Task<Void> {
        return FirebaseDatabase.getInstance().getReference("$PATH_USERS/${currentUserUID()}/productsList").setValue(productUID)
    }

    fun updatePassword(password : String): Task<Void>? {
        val user = auth.currentUser
        return user?.updatePassword(password)
    }

    fun retriveUserData() : DatabaseReference{
        var uid = currentUserUID()
        return FirebaseDatabase.getInstance().getReference("/users/$uid")
    }

    fun retrieveAnotherUser(uid : String) : DatabaseReference{
        return FirebaseDatabase.getInstance().getReference("/users/$uid")
    }

    fun currentUserUID() : String = auth.uid ?: ""

    fun retriveUserProducts(): Query {
        return FirebaseDatabase.getInstance().getReference(PATH_PRODUCTS).orderByChild("uid").equalTo(currentUserUID())
    }


//    fun updateUserProduct(){
//        return FirebaseDatabase.getInstance().getReference("$PATH_PRODUCTS")
//    }

    fun retrieveUserProductsById(uid : String): Query {
        return FirebaseDatabase.getInstance().getReference("$PATH_PRODUCTS").orderByChild("uid").equalTo(uid)
    }

    fun retrieveAllProducts(): Query {
        return FirebaseDatabase.getInstance().getReference(PATH_PRODUCTS).orderByChild("product")
    }

    fun retrieveProcuctsByValue(): Query {
        return FirebaseDatabase.getInstance().getReference(PATH_PRODUCTS).orderByChild("value")
    }

    fun retrieveRecentPosts() : Query {
        //Firebase always order um asc order, in order to return desc order we need to reverse the list in the UI
        return  FirebaseDatabase.getInstance().getReference(PATH_PRODUCTS).orderByChild("datePosted")
    }

    fun receiveProductsCloseToMe(): Query {
        return  FirebaseDatabase.getInstance().getReference(PATH_PRODUCTS)
    }

    fun receiveProductsByUf(uf : String): Query {
        return FirebaseDatabase.getInstance().getReference(PATH_PRODUCTS).orderByChild("uf").equalTo(uf)
    }

    fun receiveTopUsers() : Query{
        //Firebase always order um asc order, in order to return desc order we need to reverse the list in the UI
        return FirebaseDatabase.getInstance().getReference(PATH_USERS).orderByChild("matches")
    }

    fun receiveCategories(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference(PATH_CATEGORIES)
    }

    fun retrievebyCategories(category : String): Query {
        return FirebaseDatabase.getInstance().getReference(PATH_PRODUCTS).orderByChild("category").equalTo(category)
    }

    fun saveCurrentUIDIntoDatabase(): Task<Void> {
        return FirebaseDatabase.getInstance().getReference("$PATH_USERS/${currentUserUID()}/clientID").setValue(currentUserUID())
    }


}