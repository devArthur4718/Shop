package com.stetter.escambo.ui.core.profile

import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.auth.LoginRepository

class UpdateProfileViewModel  : ViewModel(){


    var authRepository = LoginRepository()




    fun logout(){

        authRepository.logoffUser()
    }

    fun openPickPhotoIntent() {

    }

    fun openCameraIntent() {

    }


}