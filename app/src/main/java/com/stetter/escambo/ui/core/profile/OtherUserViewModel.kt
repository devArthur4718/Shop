package com.stetter.escambo.ui.core.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.Product

class OtherUserViewModel : ViewModel() {

    val databaserepository = DatabaseRepository()

    private val _querryFirebase = MutableLiveData<ArrayList<Product>>()
    val querryFirebase: LiveData<ArrayList<Product>>get() = _querryFirebase

    fun retriveUserPostedProducts(username : String) {
        databaserepository.retriveUserProductsByName(username).addChildEventListener( object : ChildEventListener{
            var querryList = ArrayList<Product>()
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var product = p0.getValue(Product::class.java)
                product?.let { querryList.add(it) }
                _querryFirebase.value = querryList
            }

            override fun onChildRemoved(p0: DataSnapshot) { }
        })

    }

}