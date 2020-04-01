package com.stetter.escambo.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewmodel: RegisterViewModel

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

        binding.btnRegister.setOnClickListener {

        }

//        binding.edtPostalCode.addTextChangedListener(Mask.mask("#####-###", binding.edtPostalCode))
//        binding.edtPostalCode.setOnFocusChangeListener { view, b -> fetchAddress(binding.edtPostalCode) }
    }


}
