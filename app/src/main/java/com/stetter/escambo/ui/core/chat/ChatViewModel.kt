package com.stetter.escambo.ui.core.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.database.FirestoreRepository
import com.stetter.escambo.net.models.ProductInterest
import com.stetter.escambo.net.models.ProductMock
import java.lang.RuntimeException

class ChatViewModel : ViewModel() {

    val db = FirestoreRepository()

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

    private val _querryByInterest = MutableLiveData<ArrayList<ProductInterest>>()
    val querryByInterest : LiveData<ArrayList<ProductInterest>> get() = _querryByInterest

    fun fetchMyInterest(){
        db.selectMyProductsInterest().addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.w("Interest", "Listen failed.", firebaseFirestoreException)
                return@addSnapshotListener
            }

            if(querySnapshot != null){
                var querryList = ArrayList<ProductInterest>()
                for(doc in querySnapshot!!){
                    try{
                        var item = doc.toObject(ProductInterest::class.java)
                        querryList.add(item)
                    }catch (e : RuntimeException){

                    }
                }

                _querryByInterest.value = querryList
            }



        }
    }

}
