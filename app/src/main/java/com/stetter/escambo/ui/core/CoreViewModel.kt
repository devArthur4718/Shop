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

    fun showLoading(){
        _loadingProgress.value = true
    }

    fun  hideLoading(){
        _loadingProgress.value = false
    }

    //Fetch user data post login
    fun getUserDataFromDatabase() {
        database.retriveUserData().addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var user = p0.getValue(RegisterUser::class.java)
                Log.d("retrived", "userdata")
            }

        })

    }

}