package com.stetter.escambo.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewmodel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )
        viewmodel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        initViews()
    }

    private fun initViews() {

        viewmodel.navigateToRegister.observe(this, Observer {
            if(it) navigateToRegister()
        })

    }

    private fun navigateToRegister() {

    }
}
