package com.stetter.escambo.ui.recovery

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityRecoveryPasswordBinding
import com.stetter.escambo.ui.login.LoginViewModel

class RecoveryPassword : AppCompatActivity() {

    private lateinit var binding: ActivityRecoveryPasswordBinding
    private lateinit var viewmodel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery_password)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recovery_password)
        viewmodel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        initViews()

    }

    private fun initViews() {
        binding.ivUpRegister.setOnClickListener {
            finish()
        }

        binding.btnRecover.setOnClickListener {
            recoverUserPassword()
        }
    }

    private fun recoverUserPassword() {
        if (!binding.edtRecoveryEmail.text.isNullOrEmpty()) {
            var status = viewmodel.recoveryPassword(binding.edtRecoveryEmail.text.toString(), this)
            Toast.makeText(this,"Enviado para o e-mail cadastrado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
