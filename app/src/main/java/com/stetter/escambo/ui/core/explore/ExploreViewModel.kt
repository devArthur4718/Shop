package com.stetter.escambo.ui.core.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.RecentPost
import com.stetter.escambo.net.models.TopUser

class ExploreViewModel : ViewModel() {

    private val _listProduct = MutableLiveData<List<Product>>()
    val listProduct : LiveData<List<Product>> get() = _listProduct

    private val _listTopUser = MutableLiveData<List<TopUser>>()
    val listTopUser : LiveData<List<TopUser>> get() = _listTopUser

    private val _listRecentPost = MutableLiveData<List<RecentPost>>()
    val listRecentPost : LiveData<List<RecentPost>> get() = _listRecentPost


    init {

        var dummyProduct = listOf<Product>(
            Product(""),
            Product(""),
            Product(""),
            Product(""),
            Product(""),
            Product(""),
            Product(""),
            Product("")
            )

        var dummyTops = listOf<TopUser>(
            TopUser(""),
            TopUser(""),
            TopUser(""),
            TopUser(""),
            TopUser(""),
            TopUser(""))

        var dummyRecent = listOf<RecentPost>(
            RecentPost(""),
            RecentPost(""),
            RecentPost(""),
            RecentPost(""),
            RecentPost(""))

        _listProduct.value = dummyProduct
        _listTopUser.value = dummyTops
        _listRecentPost.value = dummyRecent

    }


}
