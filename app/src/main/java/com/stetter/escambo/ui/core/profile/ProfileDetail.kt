package com.stetter.escambo.ui.core.profile

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityProfileDetailBinding
import com.stetter.escambo.extension.showPickImageDialog
import com.stetter.escambo.extension.showPickImageProfile
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.ui.base.BaseActivity
import com.stetter.escambo.ui.login.LoginActivity

class ProfileDetail : BaseActivity() {

    private lateinit var binding: ActivityProfileDetailBinding
    private lateinit var viedwmodel :UpdateProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_detail)
        viedwmodel = ViewModelProviders.of(this)[UpdateProfileViewModel::class.java]
        setObservables()
    }

    private fun setObservables() {

        mainViewModel.getUserDataFromDatabase()
        mainViewModel.userProfileData.observe(this, Observer { onUserDataReceveid(it) })

        binding.ivLogout.setOnClickListener {
            viedwmodel.logout()
            var intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }


        binding.tvCloseProfileDetail.setOnClickListener {
            finish()
        }

        binding.ivChangeProfilePhoto.setOnClickListener {
            //Update photo
            showPickImageProfile(viedwmodel)
        }

    }

    private fun onUserDataReceveid(userdata: RegisterUser?) {
        //UpdateUI
        userdata?.let {
            binding.inputFullName.editText?.setText(it.fullName)
            binding.inputEmail.editText?.setText(it.email)
            binding.inputPassword.editText?.setText("******")
            binding.inputBirthDate.editText?.setText(it.birthDate)
            binding.inputPostalCode.editText?.setText(it.cep)
            binding.inputCity.editText?.setText(it.city)
            binding.inputUF.editText?.setText(it.uf)
        }


    }
}
