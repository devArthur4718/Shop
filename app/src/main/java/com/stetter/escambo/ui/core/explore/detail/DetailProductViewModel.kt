package com.stetter.escambo.ui.core.explore.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.RegisterUser

class DetailProductViewModel : ViewModel(){

    var database = DatabaseRepository()

    private val _querryProgress = MutableLiveData<RegisterUser>()
    val querryProgress: LiveData<RegisterUser> get() = _querryProgress



    fun retrieveUserInformation(uid : String){
        database.retrieveAnotherUser(uid).addValueEventListener(object  : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("Fetch User", "Error: $p0 ")
            }

            override fun onDataChange(p0: DataSnapshot) {
                try{
                    _querryProgress.value = p0.getValue(RegisterUser::class.java)!!
                }
                catch (e : KotlinNullPointerException){
                  Log.e("User", "Error : $e")
                }
            }
        })



    }
}