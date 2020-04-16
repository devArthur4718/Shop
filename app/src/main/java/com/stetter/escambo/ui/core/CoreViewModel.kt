package com.stetter.escambo.ui.core

import android.location.Location
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

    private val _dialogMessage = MutableLiveData<String>()
    val dialogMessage : LiveData<String> get() = _dialogMessage

    private val _onLocationReceived = MutableLiveData<Location>()
    val onLocationReceived : LiveData<Location> get() = _onLocationReceived


    fun showLoading(){
        _loadingProgress.value = true
    }

    fun  hideLoading(){
        _loadingProgress.value = false
    }

    fun showActivityDialog(text : String){
        _dialogMessage.value = text
    }

    fun setLocation(location : Location){
        _onLocationReceived.value = location
    }

    //Save current uid
    fun saveCurrentUID(){
        showLoading()
        database.saveCurrentUIDIntoDatabase()
            .addOnCompleteListener {

            }.addOnFailureListener {

            }
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

    private val _retrieveUserLocation = MutableLiveData<Boolean>()
    val retrieveUserLocation : LiveData<Boolean> get() = _retrieveUserLocation

    fun retrieveUserLocation(){
        _retrieveUserLocation.value = true
    }


}