package com.stetter.escambo.ui.core.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.database.FirestoreRepository
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.Product

class ProfileViewModel : ViewModel() {

    val databaserepository = DatabaseRepository()
    val db = FirestoreRepository()

    private val _querryFirebase = MutableLiveData<ArrayList<Product>>()
    val querryFirebase: LiveData<ArrayList<Product>>get() = _querryFirebase

    fun retrieveMyproducts(){
        db.selectMyProcuts().addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.w("products", "Listen failed.", firebaseFirestoreException)
                return@addSnapshotListener
            }

            if(querySnapshot != null){
                var querryList = ArrayList<Product>()
                for(doc in querySnapshot!!){
                    var item = doc.toObject(Product::class.java)
                    querryList.add(item)
                }
                _querryFirebase.value = querryList
            }
        }
    }

}
