package com.stetter.escambo.ui.core

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_core)
        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
        loginvm = ViewModelProviders.of(this)[LoginViewModel::class.java]
        corevm = ViewModelProviders.of(this)[CoreViewModel::class.java]
        initViews()
        setobservables()

    }

    private fun setobservables() {
        corevm.loadingProgress.observe(this, Observer {
            if(it){
                loadingDialog.show()
            }else{
                loadingDialog.hide()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(this)


        corevm.getUserDataFromDatabase()

    }
}
