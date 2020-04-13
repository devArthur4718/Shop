package com.stetter.escambo.ui.splash

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.stetter.escambo.R
import com.stetter.escambo.ui.core.CoreActivity
import com.stetter.escambo.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT : Long =  1000L
    private lateinit var viewmodel : SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        viewmodel = ViewModelProvider(this)[SplashViewModel::class.java]
        initViews()
    }

    private fun initViews() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                    if(report.areAllPermissionsGranted()){
                        if(viewmodel.isUserLogged()){
                            openCoreActivity()
                        }else{
                            openLoginActivity()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {

                }
            }).check()
    }

    private fun openCoreActivity(){
        Handler().postDelayed({
            startActivity(
                Intent(this,
                    CoreActivity::class.java)
            )
            finish()
        }, SPLASH_TIME_OUT)
    }
    private fun openLoginActivity() {
        Handler().postDelayed({
            startActivity(
                Intent(this,
                    LoginActivity::class.java)
            )
            finish()
        }, SPLASH_TIME_OUT)
    }
}
