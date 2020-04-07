package com.stetter.escambo.ui.core.profile

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityProfileDetailBinding
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

        binding.ivLogout.setOnClickListener {
            viedwmodel.logout()
            var intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        binding.tvCloseProfileDetail.setOnClickListener {
            finish()
        }

    }
}
