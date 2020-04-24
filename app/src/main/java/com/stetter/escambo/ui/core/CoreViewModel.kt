package com.stetter.escambo.ui.core

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.database.FirestoreRepository
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.RegisterUser

class CoreViewModel : ViewModel() {

    val database = DatabaseRepository()
    val db = FirestoreRepository()

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

    fun updateCurrentID(){
        db.updateClientID()
    }

    fun currentUserUID() : String = db.currentUserUID()

    //Fetch user data to use during app session
    fun getUserData(){
        db.selectUser().addSnapshotListener{documentSnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.w("user", "Listen failed.", firebaseFirestoreException)
                return@addSnapshotListener
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                try{
                    _userProfileData.value = documentSnapshot.toObject(RegisterUser::class.java)
                    hideLoading()
                }catch (e : KotlinNullPointerException){
                    hideLoading()
                }
                Log.d("user", "Current data: ${documentSnapshot.data}")
            } else {
                Log.d("user", "Current data: null")
            }
        }
    }

    private val _retrieveUserLocation = MutableLiveData<Boolean>()
    val retrieveUserLocation : LiveData<Boolean> get() = _retrieveUserLocation

    fun retrieveUserLocation(){
        _retrieveUserLocation.value = true
    }

    private val _userInterest = MutableLiveData<Boolean>()
    val userInterest : LiveData<Boolean> get() = _userInterest


    fun updateUserInterestList(productDocument: String) {

        db.updateInterest(productDocument)

    }


}