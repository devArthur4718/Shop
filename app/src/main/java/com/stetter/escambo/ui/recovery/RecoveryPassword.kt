package com.stetter.escambo.ui.recovery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityRecoveryPasswordBinding

class RecoveryPassword : AppCompatActivity() {

    private lateinit var binding : ActivityRecoveryPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery_password)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recovery_password)

        initViews()

    }

    private fun initViews() {
        binding.ivUpRegister.setOnClickListener {
            finish()
        }
    }
}
