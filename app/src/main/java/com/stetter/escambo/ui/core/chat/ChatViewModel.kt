package com.stetter.escambo.ui.core.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.models.Product

class ChatViewModel : ViewModel() {

    private val _listProduct = MutableLiveData<List<Product>>()
    val listProduct : LiveData<List<Product>> get() = _listProduct


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

        _listProduct.value = dummyProduct
    }

}
