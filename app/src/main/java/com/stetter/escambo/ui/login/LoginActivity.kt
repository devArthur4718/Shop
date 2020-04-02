package com.stetter.escambo.ui.login

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.stetter.escambo.databinding.ActivityLoginBinding
import com.stetter.escambo.extension.clearError
import com.stetter.escambo.ui.core.CoreActivity
import com.stetter.escambo.ui.dialog.LoadingDialog
import com.stetter.escambo.ui.recovery.RecoveryPassword
import com.stetter.escambo.ui.register.RegisterActivity
import com.stetter.escambo.R
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewmodel: LoginViewModel
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_login
        )
        viewmodel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        getKeyhash()
        initViews()
        setObservables()
    }

    private fun setObservables() {
        viewmodel.userUID.observe(this, Observer {userData ->
            userData?.let {
                if(it.isNotEmpty()) navigateCoreActivity(userData)
            }
        })

        viewmodel.loadingProgress.observe(this, Observer {
            if (it)
                loadingDialog.show()
            else
                loadingDialog.hide()
        })

        viewmodel.loginError.observe(this, Observer {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        })
    }

    private fun navigateCoreActivity(userData : String) {
        val intent = Intent(this, CoreActivity::class.java)
        intent.putExtra("uid", userData)
        finish()
        startActivity(intent)
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(this)
        setFacebookCallback()
        binding.tvCreateAccout.setOnClickListener {
            navigateToRegister()
        }
        binding.tvRecoveryPassword.setOnClickListener {
            navigateToRecover()
        }
        binding.btnLogin.setOnClickListener {
            performLogin(binding.edtLoginEmail.text.toString(), binding.edtLoginPassword.text.toString())
        }
        binding.btnPerformFaceLogin.setOnClickListener {
            binding.loginFacebook.performClick()
        }
        binding.btnPerformGoogleLogin.setOnClickListener {
            Toast.makeText(this,"Google Login", Toast.LENGTH_SHORT).show()
        }

    }

    var callbackManager = CallbackManager.Factory.create()
    private fun setFacebookCallback() {
        binding.loginFacebook.registerCallback(callbackManager, object  : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {

                handleFacebookAcesssToken(result?.accessToken)
            }

            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@LoginActivity, "Erro: ${error.toString()}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleFacebookAcesssToken(accessToken: AccessToken?) {
        accessToken?.let {  performLoginWithFacebookToken(it)  }
    }

    private fun performLogin(email: String, password: String) {
        binding.edtLoginPassword.clearError()
        binding.edtLoginEmail.clearError()
        if(!email.isNullOrEmpty() && !password.isNullOrEmpty()){
            viewmodel.showLoading()
            viewmodel.signInWithEmail(email, password, this )
        }else{
            if(binding.edtLoginEmail.text.isNullOrEmpty()) binding.edtLoginEmail.setError("")
            if(binding.edtLoginPassword.text.isNullOrEmpty()) binding.edtLoginPassword.setError("")
        }
    }
    private fun performLoginWithFacebookToken(token : AccessToken){
        viewmodel.showLoading()
        viewmodel.signInWithFacebookCredential(token)
    }

    private fun navigateToRecover() {
        val intent = Intent(this, RecoveryPassword::class.java)
        startActivity(intent)
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun getKeyhash(){
        try {
            val info = packageManager.getPackageInfo(
                "com.stetter.escambo",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode,resultCode,data)
    }
}
