package com.stetter.escambo.ui.core.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.ProductMock
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.TopUser

class ExploreViewModel : ViewModel() {

    val databaserepository = DatabaseRepository()

    private val _listProduct = MutableLiveData<List<ProductMock>>()
    val listProductMock : LiveData<List<ProductMock>> get() = _listProduct

    private val _listTopUser = MutableLiveData<List<TopUser>>()
    val listTopUser : LiveData<List<TopUser>> get() = _listTopUser

    private val _listRecentPost =MutableLiveData<ArrayList<Product>>()
    val listRecentPost : LiveData<ArrayList<Product>>get()  = _listRecentPost




    init {

        var dummyProduct = listOf<ProductMock>(
            ProductMock(""),
            ProductMock(""),
            ProductMock(""),
            ProductMock(""),
            ProductMock(""),
            ProductMock(""),
            ProductMock(""),
            ProductMock("")
            )

        var dummyTops = listOf<TopUser>(
            TopUser(""),
            TopUser(""),
            TopUser(""),
            TopUser(""),
            TopUser(""),
            TopUser(""))

        _listProduct.value = dummyProduct
        _listTopUser.value = dummyTops

    }

    fun retrieveRecentProducts(){

        databaserepository.retrieveRecentPosts().addChildEventListener(object : ChildEventListener{
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
                _listRecentPost.value = querryList

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        } )
    }

}
