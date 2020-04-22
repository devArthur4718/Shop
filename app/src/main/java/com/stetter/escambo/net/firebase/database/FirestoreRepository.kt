package com.stetter.escambo.net.firebase.database

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class FirestoreRepository {
    //Todo : Tabela produtos.
    //Todo : Editar ou excluir o produto de um usuário.

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    companion object{

        const val DOCUMENT_USERS = "users"
        const val DOCUMENT_PRODUCTS = "user-products"

        const val FIELD_PHOTO_URL = "photoURL"
        const val FIELD_DATE_POSTED = "datePosted"
        const val FIELD_PRODUCTS_FIELD = "products"
        const val USER_ID_FIELD = "clientID"
    }

    fun currentUserUID() : String = auth.uid ?: ""

    //region User
    fun insertUser(): CollectionReference {
        // Add a new document with a generated ID
       return db.collection("users")
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

    //endregion


}