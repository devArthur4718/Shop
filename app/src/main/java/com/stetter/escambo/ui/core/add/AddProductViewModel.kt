package com.stetter.escambo.ui.core.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddProductViewModel : ViewModel() {

    private val _listCategoryProduct = MutableLiveData<List<String>>()
    val listCategorList : LiveData<List<String>> get() = _listCategoryProduct

    init {

        var dummyList = listOf<String>("Categoria", "Produto 1", "Produto 2", "Produto 3", "Produto 4")

        _listCategoryProduct.value = dummyList
    }

}
