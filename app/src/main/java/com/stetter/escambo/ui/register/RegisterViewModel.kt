package com.stetter.escambo.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.models.UserAddress


class RegisterViewModel : ViewModel() {

    private val _addressValue = MutableLiveData<UserAddress>()
    val addressValue : LiveData<UserAddress> get() = _addressValue


}