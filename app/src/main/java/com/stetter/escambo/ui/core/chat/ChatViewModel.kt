package com.stetter.escambo.ui.core.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.models.ProductMock

class ChatViewModel : ViewModel() {

    private val _listProduct = MutableLiveData<List<ProductMock>>()
    val listProductMock : LiveData<List<ProductMock>> get() = _listProduct


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

        _listProduct.value = dummyProduct
    }

}
