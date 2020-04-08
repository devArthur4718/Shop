package com.stetter.escambo.ui.core.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.RecentPost
import com.stetter.escambo.net.models.SendProduct

class ProfileViewModel : ViewModel() {

    val databaserepository = DatabaseRepository()

    private val _listProduct = MutableLiveData<List<Product>>()
    val listProduct: LiveData<List<Product>> get() = _listProduct

    private val _listRecentPost = MutableLiveData<List<RecentPost>>()
    val listRecentPost: LiveData<List<RecentPost>> get() = _listRecentPost


    init {
        var dummyRecent = listOf<RecentPost>(
            RecentPost(""),
            RecentPost(""),
            RecentPost(""),
            RecentPost(""),
            RecentPost("")
        )

        _listRecentPost.value = dummyRecent
    }

    fun retriveUserPostedProducts() {
        databaserepository.retriveUserProducts().addChildEventListener(object : ChildEventListener {
            var querryList = ArrayList<SendProduct>()
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                var product = p0.getValue(SendProduct::class.java)
            }
            override fun onChildChanged(datasnapshot: DataSnapshot, p1: String?) {
                var product = datasnapshot.getValue(SendProduct::class.java)

            }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var product = p0.getValue(SendProduct::class.java)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var product = p0.getValue(SendProduct::class.java)
            }
        })

    }


}
