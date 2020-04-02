package com.stetter.escambo.ui.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityCoreBinding
import com.stetter.escambo.ui.login.LoginViewModel

class CoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoreBinding
    private lateinit var loginvm: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_core)
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
        loginvm = ViewModelProviders.of(this)[LoginViewModel::class.java]
        getBundle()
        initViews()
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }


    private fun getBundle() {

    }

    private fun initViews() {

    }
}
