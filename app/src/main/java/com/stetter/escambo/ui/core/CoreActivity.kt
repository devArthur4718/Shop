package com.stetter.escambo.ui.core

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityCoreBinding
import com.stetter.escambo.extension.isGPsEnabled
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
        retrieveLocation()

    }

    override fun onResume() {
        super.onResume()

    }

    private fun setobservables() {
        corevm.saveCurrentUID()
        corevm.loadingProgress.observe(this, Observer {
            if(it){
                loadingDialog.show()
            }else{
                loadingDialog.hide()
            }
        })

        corevm.getUserDataFromDatabase()

        //Utils
        corevm.dialogMessage.observe(this, Observer { onShowDialogMessage(it) })

    }

    //User location used to retrieve products next to him
    private fun retrieveLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.\
                if(location == null){
                    //Check enabled, if not ,call again
                    if(!isGPsEnabled()){
                        //Prompt user.
                        showDialog("GPS desativado. Alguns serviços não funcionaram corretamente! Deseja ativar?").show()
                    }else{
                        retrieveLocation()
                    }

                }else{
                    //Call update lists in fragment
                    corevm.setLocation(location)
                }
            }.addOnFailureListener { error ->
                Log.e("Explore", "Error: $error")
            }
    }

    //Show
    private fun onShowDialogMessage(text: String?) {
        text?.let { text -> showDialog(text).show() }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(this)

    }

    private fun showDialog(text: String): AlertDialog {
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        var callLocationSetting = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivityForResult(callLocationSetting, RQ_GPS_SETTINGS)
                        dialog.dismiss()
                    })
                setNegativeButton(
                    "Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                      dialog.dismiss()
                    })

                setMessage(text)
            }
            builder.create()
        }
        return alertDialog!!
    }
    companion object{
        const val RQ_GPS_SETTINGS = 98
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RQ_GPS_SETTINGS -> {
                //Returned from GPS settings
                retrieveLocation()

            }
        }
    }
}
