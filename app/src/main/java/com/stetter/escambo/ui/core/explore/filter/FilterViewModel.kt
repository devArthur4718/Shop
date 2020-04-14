package com.stetter.escambo.ui.core.explore.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.Product

class FilterViewModel : ViewModel(){

    val database = DatabaseRepository()

    private val _querryByName = MutableLiveData<ArrayList<Product>>()
    val querryByName: LiveData<ArrayList<Product>>   get() = _querryByName

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean>   get() = _loadingProgress

    fun searchProduct(){
        _loadingProgress.value = true
        database.retrieveAllProducts().addChildEventListener(object  : ChildEventListener{
            var querryList = ArrayList<Product>()
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var product = p0.getValue(Product::class.java)
                product?.let { querryList.add(it) }
                _querryByName.value = querryList
            }

            override fun onChildRemoved(p0: DataSnapshot) { }
        })


    }
    private val _querryByValues = MutableLiveData<ArrayList<Product>>()
    val queryByValues: LiveData<ArrayList<Product>>   get() = _querryByValues

    fun searchByValue() {
        _loadingProgress.value = true
        database.retrieveProcuctsByValue().addChildEventListener(object  : ChildEventListener{
            var querryList = ArrayList<Product>()
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                _loadingProgress.value = false
                var product = p0.getValue(Product::class.java)
                product?.let { querryList.add(it) }
                _querryByValues.value = querryList
            }
            override fun onChildRemoved(p0: DataSnapshot)  {}
        })
    }

    fun searchByProximity(){

    }

    fun hideProgress(){
        _loadingProgress.value = false
    }

}