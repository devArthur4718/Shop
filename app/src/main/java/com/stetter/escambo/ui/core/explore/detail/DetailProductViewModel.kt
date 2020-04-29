package com.stetter.escambo.ui.core.explore.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.database.FirestoreRepository
import com.stetter.escambo.net.models.ProductInterest
import com.stetter.escambo.net.models.RegisterUser

class DetailProductViewModel : ViewModel() {


    val db = FirestoreRepository()

    private val _querryProgress = MutableLiveData<RegisterUser>()
    val querryProgress: LiveData<RegisterUser> get() = _querryProgress

    private val _interestRequest = MutableLiveData<String>()
    val interestRequest: LiveData<String> get() = _interestRequest

    fun retrieveUserInformation(uid: String) {
        db.selectAnotherUser(uid)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w("user", "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    try {
                        _querryProgress.value = documentSnapshot.toObject(RegisterUser::class.java)

                    } catch (e: KotlinNullPointerException) {
                        Log.d("user", "Current data: null")
                    }
                    Log.d("user", "Current data: ${documentSnapshot.data}")
                } else {
                    Log.d("user", "Current data: null")
                }
            }
    }

    private val _interestSuccess = MutableLiveData<Boolean>()
    val interestSuccess: LiveData<Boolean> get() = _interestSuccess


    fun sendProductInterest(interest: ProductInterest) {
        db.sendInterest().document(interest.productKey).set(interest)
            .addOnSuccessListener {
                //TODO : inform user that data has been sent
                //TODO : Finish activity
                _interestSuccess.value = true
                Log.d("Interest", "Success")
            }
            .addOnFailureListener {
                _interestSuccess.value = false
                Log.e("Interest", "Error : $it")

            }

    }


}