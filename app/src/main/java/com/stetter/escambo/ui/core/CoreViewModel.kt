package com.stetter.escambo.ui.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CoreViewModel : ViewModel() {

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress : LiveData<Boolean> get() = _loadingProgress

    fun showLoading(){
        _loadingProgress.value = true
    }

    fun  hideLoading(){
        _loadingProgress.value = false
    }

}