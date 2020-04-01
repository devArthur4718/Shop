package com.stetter.escambo.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityLoginBinding
import com.stetter.escambo.extension.clearError
import com.stetter.escambo.ui.core.CoreActivity
import com.stetter.escambo.ui.recovery.RecoveryPassword
import com.stetter.escambo.ui.register.RegisterActivity

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
        setObservables()
    }

    private fun setObservables() {
        viewmodel.navigateToHome.observe(this, Observer {
            if(it){
                navigateCoreActivity()
            }
        })

    }

    private fun navigateCoreActivity() {
        val intent = Intent(this, CoreActivity::class.java)
        viewmodel.finishedNavigateToHome()
        finish()
        startActivity(intent)
    }

    private fun initViews() {
        binding.tvCreateAccout.setOnClickListener {
            navigateToRegister()
        }
        binding.tvRecoveryPassword.setOnClickListener {
            navigateToRecover()
        }
        binding.btnLogin.setOnClickListener {
            performLogin(binding.edtLoginEmail.text.toString(), binding.edtLoginPassword.text.toString())
        }
    }

    private fun performLogin(email: String, password: String) {
        binding.edtLoginPassword.clearError()
        binding.edtLoginEmail.clearError()
        if(!email.isNullOrEmpty() && !password.isNullOrEmpty()){
            viewmodel.signInWithEmail(email, password, this )
        }else{
            if(binding.edtLoginEmail.text.isNullOrEmpty()) binding.edtLoginEmail.setError("valor obrigatório")
            if(binding.edtLoginPassword.text.isNullOrEmpty()) binding.edtLoginPassword.setError("valor obrigatório")
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
