package com.stetter.escambo.ui.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.RegisterUser

class CoreViewModel : ViewModel() {

    val database = DatabaseRepository()

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress : LiveData<Boolean> get() = _loadingProgress

    private val _userProfileData = MutableLiveData<RegisterUser>()
    val userProfileData : LiveData<RegisterUser> get() = _userProfileData

    private val _userLat = MutableLiveData<Double>()
    val userLat : LiveData<Double> get() = _userLat

    private val _userLng = MutableLiveData<Double>()
    val userLng : LiveData<Double> get() = _userLng


    fun showLoading(){
        _loadingProgress.value = true
    }

    fun  hideLoading(){
        _loadingProgress.value = false
    }

    //Fetch user data post login
    fun getUserDataFromDatabase() {
        showLoading()
        database.retriveUserData().addValueEventListener(object  : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.e("Fetch User", "Error: $p0 ")

            }

            override fun onDataChange(p0: DataSnapshot) {
                try{
                    _userProfileData.value = p0.getValue(RegisterUser::class.java)!!
                    hideLoading()
                }
                catch (e : KotlinNullPointerException){
                    hideLoading()
                }
            }


        })

    }

}