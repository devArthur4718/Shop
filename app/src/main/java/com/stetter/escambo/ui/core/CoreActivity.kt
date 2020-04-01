package com.stetter.escambo.ui.core

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityCoreBinding
import com.stetter.escambo.ui.login.LoginViewModel

class CoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoreBinding
    private lateinit var loginvm: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_core)
        loginvm = ViewModelProviders.of(this)[LoginViewModel::class.java]
        initViews()
    }

    private fun initViews() {
        binding.btnLogoff.setOnClickListener {
            loginvm.logoff()
            finish()
        }
    }
}
