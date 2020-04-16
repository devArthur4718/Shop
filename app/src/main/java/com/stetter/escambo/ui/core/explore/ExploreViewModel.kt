package com.stetter.escambo.ui.core.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.ProductByLocation
import com.stetter.escambo.net.models.RegisterUser


class ExploreViewModel : ViewModel() {

    val databaserepository = DatabaseRepository()

    private val _listNextProducts = MutableLiveData<List<ProductByLocation>>()
    val listNextProducts : LiveData<List<ProductByLocation>> get() = _listNextProducts

    private val _listRecentPost =MutableLiveData<ArrayList<Product>>()
    val listRecentPost : LiveData<ArrayList<Product>>get()  = _listRecentPost

    private val _topUserLists = MutableLiveData<ArrayList<RegisterUser>>()
    val topUsersList : LiveData<ArrayList<RegisterUser>> get() = _topUserLists



    fun retrieveRecentProducts(){
        databaserepository.retrieveRecentPosts().addChildEventListener(object : ChildEventListener{
            var querryList = ArrayList<Product>()
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildAdded(datasnapshopt: DataSnapshot, p1: String?) {
                var products = datasnapshopt.getValue(Product::class.java)
                products?.let { data ->  querryList.add(data) }
                _listRecentPost.value = querryList

            }
            override fun onChildRemoved(p0: DataSnapshot) { }
        } )
    }

    fun retrieveTopUsers(){
        databaserepository.receiveTopUsers().addChildEventListener(object  : ChildEventListener{
            var topUserList = ArrayList<RegisterUser>()
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {  }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val users = dataSnapshot.getValue(RegisterUser::class.java)
                users?.let { data -> topUserList.add(data)  }
                _topUserLists.value = topUserList
            }
            override fun onChildRemoved(p0: DataSnapshot) { }

        })
    }

    fun retrieveProductsCloseToMe(){
        databaserepository.receiveProductsCloseToMe().addChildEventListener(object  : ChildEventListener{
            var querryList = ArrayList<ProductByLocation>()
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
               val productsClose = dataSnapshot.getValue(ProductByLocation::class.java)
                productsClose?.let { data -> querryList.add(data) }
                _listNextProducts.value = querryList
            }
            override fun onChildRemoved(p0: DataSnapshot) { }
        })
    }

    fun retrieveUserUID(): String = databaserepository.currentUserUID()

}
