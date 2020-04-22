package com.stetter.escambo.ui.core.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.database.FirestoreRepository
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.ProductByLocation
import com.stetter.escambo.net.models.RegisterUser


class ExploreViewModel : ViewModel() {

    val databaserepository = DatabaseRepository()
    val db = FirestoreRepository()

    private val _listNextProducts = MutableLiveData<List<ProductByLocation>>()
    val listNextProducts : LiveData<List<ProductByLocation>> get() = _listNextProducts

    private val _listRecentPost =MutableLiveData<ArrayList<Product>>()
    val listRecentPost : LiveData<ArrayList<Product>>get()  = _listRecentPost

    private val _topUserLists = MutableLiveData<ArrayList<RegisterUser>>()
    val topUsersList : LiveData<ArrayList<RegisterUser>> get() = _topUserLists



    fun selectProducts(){
        db.selectRecentPostedProducts().addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.w("products", "Listen failed.", firebaseFirestoreException)
                return@addSnapshotListener
            }

            if(querySnapshot != null){
                var querryList = ArrayList<Product>()
                for(doc in querySnapshot!!){
                    var item = doc.toObject(Product::class.java)
                    querryList.add(item)
                }
                _listRecentPost.value = querryList

            }
        }
    }


    fun retrieveMostRatedUsers(){
        db.selectTopUsers().addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.w("products", "Listen failed.", firebaseFirestoreException)
                return@addSnapshotListener
            }


            if(querySnapshot != null){
                var topUserList = ArrayList<RegisterUser>()
                for(doc in querySnapshot){
                    var item = doc.toObject(RegisterUser::class.java)
                    topUserList.add(item)
                }
                _topUserLists.value = topUserList

            }

        }
    }

    fun retrieveProductsNextToMe(){
        db.selectRecentPostedProducts().addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.w("products", "Listen failed.", firebaseFirestoreException)
                return@addSnapshotListener
            }

            if(querySnapshot != null){
                var querryList = ArrayList<ProductByLocation>()
                for(doc in querySnapshot!!){
                    var item = doc.toObject(ProductByLocation::class.java)
                    querryList.add(item)
                }
                _listNextProducts.value = querryList

            }
        }

    }


    fun retrieveUserUID(): String = databaserepository.currentUserUID()

}
