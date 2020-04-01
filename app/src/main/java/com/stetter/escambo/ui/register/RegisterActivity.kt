package com.stetter.escambo.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var viewmodel : RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        viewmodel = ViewModelProviders.of(this)[RegisterViewModel::class.java]
        initViews()
    }

    private fun initViews() {

        binding.ivUpRegister.setOnClickListener {
            finish()
        }
    }
}
