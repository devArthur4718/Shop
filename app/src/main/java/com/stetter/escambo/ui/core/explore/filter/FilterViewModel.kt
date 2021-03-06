package com.stetter.escambo.ui.core.explore.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.stetter.escambo.net.firebase.database.FirestoreRepository
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.ProductByLocation
import com.stetter.escambo.net.retrofit.api.IBGEapi
import com.stetter.escambo.net.retrofit.responses.CityResponseItem
import com.stetter.escambo.net.retrofit.responses.UfsResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class FilterViewModel : ViewModel(){

    val database = DatabaseRepository()
    val db = FirestoreRepository()

    private val _querryByName = MutableLiveData<ArrayList<Product>>()
    val querryByName: LiveData<ArrayList<Product>>   get() = _querryByName

    private val _querryByCategories = MutableLiveData<ArrayList<Product>>()
    val querryCategories: LiveData<ArrayList<Product>>   get() = _querryByCategories

    private val _querryByLocation = MutableLiveData<ArrayList<ProductByLocation>>()
    val querryByLocation: LiveData<ArrayList<ProductByLocation>>   get() = _querryByLocation

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean>   get() = _loadingProgress

    private val _listCategoryProduct = MutableLiveData<List<String>>()
    val listCategoryList: LiveData<List<String>> get() = _listCategoryProduct

    private val _listUfs = MutableLiveData<List<UfsResponseItem>>()
    val listUfs: LiveData<List<UfsResponseItem>> get() = _listUfs
    var querryList = ArrayList<Product>()

//    fun searchProduct(){
//        _loadingProgress.value = true
//        database.retrieveAllProducts().addChildEventListener(object  : ChildEventListener{
//            var querryList = ArrayList<Product>()
//            override fun onCancelled(p0: DatabaseError) { }
//            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
//            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
//            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
//                var product = p0.getValue(Product::class.java)
//                product?.let { querryList.add(it) }
//                _querryByName.value = querryList
//            }
//
//            override fun onChildRemoved(p0: DataSnapshot) { }
//        })
//    }

    fun searchByProductName(productName : String){
        _loadingProgress.value = true
        db.searchByName(productName).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.w("products", "Listen failed.", firebaseFirestoreException)
                return@addSnapshotListener
            }

            if(querySnapshot != null) {
                var querrylist = ArrayList<Product>()
                for(doc in querySnapshot!!){
                    try{
                        var item = doc.toObject(Product::class.java)
                        querrylist.add(item)

                    }catch (e : RuntimeException){

                    }
                }
                _querryByName.value = querrylist

            }

        }


    }

    private val _querryByValues = MutableLiveData<ArrayList<Product>>()
    val queryByValues: LiveData<ArrayList<Product>>   get() = _querryByValues

    var minValue = 0.0
    var maxValue = 0.0
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

    fun hideProgress(){
        _loadingProgress.value = false
    }

    fun fetchCategories() {
        database.receiveCategories().addChildEventListener(object  : ChildEventListener{
            var categoriesList = ArrayList<String>()
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var text = p0.getValue(String::class.java)
                text?.let { categoriesList.add(it) }
                _listCategoryProduct.value  = categoriesList
            }
            override fun onChildRemoved(p0: DataSnapshot) { }
        })

    }

    fun searchByCategory(category : String) {
        _loadingProgress.value = true
        querryList.clear()
        database.retrievebyCategories(category).addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                _loadingProgress.value = false
                var product = p0.getValue(Product::class.java)
                product?.let { querryList.add(it) }
                _querryByCategories.value = querryList
            }
            override fun onChildRemoved(p0: DataSnapshot) { }
        })
    }

    var maxDistance = 0
    fun searchByLocalization() {
        _loadingProgress.value = true
        querryList.clear()
        database.receiveProductsCloseToMe().addChildEventListener(object  : ChildEventListener{
            var querryList = ArrayList<ProductByLocation>()
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val productsClose = dataSnapshot.getValue(ProductByLocation::class.java)
                productsClose?.let { data -> querryList.add(data) }
                _querryByLocation.value = querryList
            }
            override fun onChildRemoved(p0: DataSnapshot) { }
        })
    }

    fun fetchUFsIds() {
        _loadingProgress.value = true
        IBGEapi.IBGESservice.getUfsIds().enqueue(object  : Callback<ArrayList<UfsResponseItem>>{
            override fun onFailure(call: Call<ArrayList<UfsResponseItem>>, t: Throwable) {
                _loadingProgress.value = false
                Log.e("Filter", "Error : $t")
            }
            override fun onResponse(
                call: Call<ArrayList<UfsResponseItem>>,
                response: Response<ArrayList<UfsResponseItem>>
            ) {
                _loadingProgress.value = false
                if(response.isSuccessful){
                  _listUfs.value = response.body()
                }
            }
        })


        }

    private val _listCities = MutableLiveData<ArrayList<String>>()
    val listCities: LiveData<ArrayList<String>> get() = _listCities

    var citiList = ArrayList<String>()
    fun fetchCities(id : Int) {
        IBGEapi.IBGESservice.getCities(id).enqueue(object  : Callback<ArrayList<CityResponseItem>> {
            override fun onFailure(call: Call<ArrayList<CityResponseItem>>, t: Throwable) {
                Log.e("Filter", "Error : $t")
            }

            override fun onResponse(
                call: Call<ArrayList<CityResponseItem>>,
                response: Response<ArrayList<CityResponseItem>>
            ) {
                if(response.isSuccessful){
                    var result = response.body()
                    citiList.clear()
                    result?.forEach {
                        citiList.add(it.nome)
                    }
                    _listCities.value = citiList

                }
            }
        })

    }

    fun retrieveUserUID(): String = database.currentUserUID()

    private val _queryByUf = MutableLiveData<ArrayList<ProductByLocation>>()
    val querryByUf: LiveData<ArrayList<ProductByLocation>>   get() = _queryByUf

    var city : String = ""
    fun searchByUfCity(uf : String) {
        _loadingProgress.value = true
        querryList.clear()
        database.receiveProductsByUf(uf).addChildEventListener(object  : ChildEventListener{
            var querryList = ArrayList<ProductByLocation>()
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val productsClose = dataSnapshot.getValue(ProductByLocation::class.java)
                productsClose?.let { data -> querryList.add(data) }
                _queryByUf.value = querryList
            }
            override fun onChildRemoved(p0: DataSnapshot) { }
        })

    }


}

