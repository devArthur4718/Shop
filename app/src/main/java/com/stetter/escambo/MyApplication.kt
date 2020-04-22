package com.stetter.escambo

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.stetho.Stetho

class MyApplication: Application(), CameraXConfig.Provider {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
//        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}