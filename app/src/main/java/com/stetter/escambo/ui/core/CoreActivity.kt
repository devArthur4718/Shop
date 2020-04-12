package com.stetter.escambo.ui.core

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityCoreBinding
import com.stetter.escambo.ui.base.BaseActivity
import com.stetter.escambo.ui.dialog.LoadingDialog
import com.stetter.escambo.ui.login.LoginViewModel

class CoreActivity : BaseActivity() {

    private lateinit var binding: ActivityCoreBinding
    private lateinit var loginvm: LoginViewModel
    private lateinit var corevm : CoreViewModel
    private lateinit var loadingDialog: LoadingDialog
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_core)
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
        loginvm = ViewModelProvider(this)[LoginViewModel::class.java]
        corevm = ViewModelProvider(this)[CoreViewModel::class.java]
        initViews()
        setobservables()
        lastknowLocationService()
    }

    private fun lastknowLocationService() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    fun retriverLastKnowLocation(): Task<Location> {
      return fusedLocationClient.lastLocation
    }

    private fun setobservables() {
        corevm.loadingProgress.observe(this, Observer {
            if(it){
                loadingDialog.show()
            }else{
                loadingDialog.hide()
            }
        })

        corevm.getUserDataFromDatabase()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(this)

    }
}
