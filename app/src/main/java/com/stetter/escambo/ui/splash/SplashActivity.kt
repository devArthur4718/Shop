package com.stetter.escambo.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
        Handler().postDelayed({
            startActivity(
                Intent(this,
                    LoginActivity::class.java)
            )
            finish()
        }, SPLASH_TIME_OUT)
    }
}