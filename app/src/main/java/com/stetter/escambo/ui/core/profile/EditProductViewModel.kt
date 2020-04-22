package com.stetter.escambo.ui.core.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.stetter.escambo.net.firebase.database.FirestoreRepository
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.Product

class EditProductViewModel : ViewModel() {
    val databaserepository = DatabaseRepository()
    val db = FirestoreRepository()

    private val _listCategoryProduct = MutableLiveData<List<String>>()
    val listCategoryList: LiveData<List<String>> get() = _listCategoryProduct

    private val _onDeletedProduct = MutableLiveData<Boolean>()
    val onDeletedProduct: LiveData<Boolean> get() = _onDeletedProduct

    fun fetchProductCategories(){
        var categoriesList = ArrayList<String>()
        databaserepository.receiveCategories().addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildAdded(data: DataSnapshot, p1: String?) {
                var text = data.getValue(String::class.java)
                text?.let { categoriesList.add(it) }
                _listCategoryProduct.value  = categoriesList
            }
            override fun onChildRemoved(p0: DataSnapshot) { }
        })
    }

    private val _onProductUpdate = MutableLiveData<Boolean>()
    val onProductUpdate: LiveData<Boolean> get() = _onProductUpdate


    fun updateProduct(product : Product){
        db.updateUserProduct(product.productKey).set(product)
            .addOnSuccessListener {
                _onProductUpdate.value = true
            }
            .addOnFailureListener {
                _onProductUpdate.value = false
            }
    }

    fun deleteProduct(productKey : String){
        db.deleteUserProduct(productKey)
            .addOnSuccessListener {
                _onDeletedProduct.value = true
            }
            .addOnFailureListener {
                _onDeletedProduct.value = false
            }
    }

    fun getUid(): String {
        return databaserepository.currentUserUID()
    }
}