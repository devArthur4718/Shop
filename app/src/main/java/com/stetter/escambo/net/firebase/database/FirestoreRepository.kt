package com.stetter.escambo.net.firebase.database

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class FirestoreRepository {


    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    companion object{

        const val DOCUMENT_USERS = "users"
        const val DOCUMENT_PRODUCTS = "user-products"
        const val DOCUMENT_PRODUCT_INTEREST = "product-interest"
        const val FIELD_PHOTO_URL = "photoURL"
        const val FIELD_DATE_POSTED = "datePosted"
        const val FIELD_PRODUCTS_FIELD = "products"
        const val USER_ID_FIELD = "clientID"
    }

    fun currentUserUID() : String = auth.uid ?: ""

    //region User
    fun insertUser(): CollectionReference {
        // Add a new document with a generated ID
       return db.collection(DOCUMENT_USERS)
    }
    fun selectUser(): DocumentReference {
        return db.collection(DOCUMENT_USERS).document(currentUserUID())
    }

    fun selectAnotherUser(uid : String): DocumentReference {
        return db.collection(DOCUMENT_USERS).document(uid)
    }

    fun updateClientID(){
         db.collection(DOCUMENT_USERS).document(currentUserUID()).update(USER_ID_FIELD, currentUserUID())
    }

    fun selectTopUsers(): Query {
        return db.collection(DOCUMENT_USERS).orderBy(FIELD_PRODUCTS_FIELD, Query.Direction.DESCENDING)
    }

    fun updateUser(): DocumentReference {
        return db.collection(DOCUMENT_USERS).document(currentUserUID())
    }

    fun updateUserPhoto(photourl : String){
        db.collection(DOCUMENT_USERS).document(currentUserUID()).update(FIELD_PHOTO_URL, photourl)
    }

    fun updateProductCount(count : Int): Task<Void> {
      return  db.collection(DOCUMENT_USERS).document(currentUserUID()).update(FIELD_PRODUCTS_FIELD, count)
    }
    //endregion


    //region Product
    fun insertProduct(): CollectionReference {
        return db.collection(DOCUMENT_PRODUCTS)
    }

    fun selectMyProcuts(): Query {
        return db.collection(DOCUMENT_PRODUCTS).whereEqualTo("uid", currentUserUID())
    }

    fun selectProductsByUID(uid : String): Query {
        return db.collection(DOCUMENT_PRODUCTS).whereEqualTo("uid", uid)
    }

    fun selectRecentPostedProducts(): Query {
       return  db.collection(DOCUMENT_PRODUCTS).orderBy(FIELD_DATE_POSTED, Query.Direction.DESCENDING)
    }

    fun updateUserProduct(productKey : String): DocumentReference {
        return db.collection(DOCUMENT_PRODUCTS).document(productKey)
    }

    fun deleteUserProduct(productKey : String): Task<Void> {
        return db.collection(DOCUMENT_PRODUCTS).document(productKey).delete()
    }

    fun sendInterest(): CollectionReference {
        return  db.collection(DOCUMENT_PRODUCT_INTEREST)
    }

    fun updateInterest(productDocument: String): Task<Void> {
        return db.collection(DOCUMENT_PRODUCT_INTEREST).document(productDocument).set("uid")
    }

    //endregion

    //region Filter Products

    fun searchByName( name : String): Query {
       return db.collection(DOCUMENT_PRODUCTS).whereGreaterThanOrEqualTo(FIELD_PRODUCTS_FIELD, name)

    }

    fun searchByValue() {

    }

    fun searchByCategory(){

    }

    fun searchByLocalization(){

    }


    //endregion
}