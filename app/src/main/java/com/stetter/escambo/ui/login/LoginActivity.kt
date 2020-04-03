package com.stetter.escambo.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.stetter.escambo.BuildConfig
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityLoginBinding
import com.stetter.escambo.extension.clearError
import com.stetter.escambo.ui.core.CoreActivity
import com.stetter.escambo.ui.dialog.LoadingDialog
import com.stetter.escambo.ui.recovery.RecoveryPassword
import com.stetter.escambo.ui.register.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions
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
        initViews()
        setObservables()
    }

    private fun setObservables() {
        viewmodel.userUID.observe(this, Observer { userData ->
            userData?.let {
                if (it.isNotEmpty()) navigateCoreActivity(userData)
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

    private fun navigateCoreActivity(userData: String) {
        val intent = Intent(this, CoreActivity::class.java)
        intent.putExtra("uid", userData)
        finish()
        startActivity(intent)
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(this)
        setFacebookCallback()
        setGoogleCallback()
        binding.tvCreateAccout.setOnClickListener {
            navigateToRegister()
        }
        binding.tvRecoveryPassword.setOnClickListener {
            navigateToRecover()
        }
        binding.btnLogin.setOnClickListener {
            performLogin(
                binding.edtLoginEmail.text.toString(),
                binding.edtLoginPassword.text.toString()
            )
        }
        binding.btnPerformFaceLogin.setOnClickListener {
            binding.loginFacebook.performClick()
        }
        binding.btnPerformGoogleLogin.setOnClickListener {
            handleGoogleSign()
        }

        if(BuildConfig.DEBUG){
            binding.edtLoginEmail.setText("devarthur4718@gmail.com")
            binding.edtLoginPassword.setText("12345678")
        }


    }

    private fun setGoogleCallback() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    var callbackManager = CallbackManager.Factory.create()
    private fun setFacebookCallback() {
        binding.loginFacebook.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {

                    handleFacebookAcesssToken(result?.accessToken)
                }

                override fun onCancel() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onError(error: FacebookException?) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Erro: ${error.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun handleFacebookAcesssToken(accessToken: AccessToken?) {
        accessToken?.let { performLoginWithFacebookToken(it) }
    }

    private fun performLogin(email: String, password: String) {
        binding.edtLoginPassword.clearError()
        binding.edtLoginEmail.clearError()
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            viewmodel.showLoading()
            viewmodel.signInWithEmail(email, password)
        } else {
            if (binding.edtLoginEmail.text.isNullOrEmpty()) binding.edtLoginEmail.setError("")
            if (binding.edtLoginPassword.text.isNullOrEmpty()) binding.edtLoginPassword.setError("")
        }
    }

    private fun performLoginWithFacebookToken(token: AccessToken) {
        viewmodel.showLoading()
        viewmodel.signInWithFacebookCredential(token)
    }

    private fun navigateToRecover() {
        loadingDialog.hide()
        val intent = Intent(this, RecoveryPassword::class.java)
        startActivity(intent)
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) { // The Task returned from this call is always completed, no need to attach

            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)
                account?.let { account ->  firebaseAuthWithGoogle(account) }


            } catch (e: ApiException) {
                Toast.makeText(this, "Login failed: ${e}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        viewmodel.showLoading()
        viewmodel.signInWithGoogleCredential(account)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.loginGoogle -> handleGoogleSign()
        }
    }

    private fun handleGoogleSign() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    companion object {
        const val RC_SIGN_IN = 10
    }
}
