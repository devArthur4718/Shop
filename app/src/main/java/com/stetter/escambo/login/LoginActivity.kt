package com.stetter.escambo.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityLoginBinding
import com.stetter.escambo.recovery.RecoveryPassword
import com.stetter.escambo.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewmodel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_login
        )
        viewmodel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        initViews()
    }

    private fun initViews() {
        binding.tvCreateAccout.setOnClickListener {
            navigateToRegister()
        }
        binding.tvRecoveryPassword.setOnClickListener {
            navigateToRecover()
        }

    }

    private fun navigateToRecover() {
        val intent = Intent(this, RecoveryPassword::class.java)
        startActivity(intent)
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
