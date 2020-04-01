package com.stetter.escambo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT : Long = 3000

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
