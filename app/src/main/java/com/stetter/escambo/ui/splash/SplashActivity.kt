package com.stetter.escambo.ui.splash

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.stetter.escambo.BuildConfig
import com.stetter.escambo.R
import com.stetter.escambo.ui.login.LoginActivity


class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT : Long = if(BuildConfig.DEBUG) 0L else 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        initViews()
    }

    private fun initViews() {

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    openLoginActivity()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    openLoginActivity()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()


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
