package com.stetter.escambo.ui.register

import android.app.AlertDialog
import android.content.DialogInterface
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityRegisterNewBinding
import com.stetter.escambo.extension.*
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.net.retrofit.responses.postalResponse
import com.stetter.escambo.ui.dialog.LoadingDialog
import com.stetter.escambo.ui.dialogs.CustomDialog
import java.io.IOException


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterNewBinding
    private lateinit var viewmodel: RegisterViewModel
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var errorDialog: CustomDialog
    var latitude  = 0.0
    var longitute = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_new)
        viewmodel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.lifecycleOwner = this
        initViews()
        setObservables()

    }

    private fun setObservables() {
        viewmodel.loadingProgress.observe(this, Observer {
            if (it)
                loadingDialog.show()
            else
                loadingDialog.hide()
        })

        viewmodel.addressValue.observe(this, Observer { response -> onAddressReceived(response) })

        viewmodel.showErrorDialog.observe(this, Observer {
            if (it) {
                var alert = showErrorDialog()
                alert.show()
            }

        })

        viewmodel.showRegisterError.observe(this, Observer { onRegisterError(it) })

        viewmodel.registerObserver.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Cadastro Enviado", Toast.LENGTH_SHORT).show()
                finish()

            }
        })

        binding.tvFinishIt.setOnClickListener {
            finish()
        }
        binding.tvLabelHasAccount.setOnClickListener {
            binding.tvFinishIt.performClick()
        }

    }

    private fun onAddressReceived(response: postalResponse) {
        hideKeyBoard(binding.inputPostalCode)
        if (response != null) {
            binding.inputCity.editText?.setText(response.localidade)
            binding.inputUF.editText?.setText(response.uf)
            //Lat long ref

            if (Geocoder.isPresent()) {
                try {
                    latitude = GeocoderLocation(binding.inputCity.editText!!.text.toString()).first
                    longitute = GeocoderLocation(binding.inputCity.editText!!.text.toString()).second

                } catch (e: IOException) {
                    Log.e("Register", "Error : $e")
                }
            }
        }
    }

    private fun initViews() {
        loadingDialog = LoadingDialog(this)
        errorDialog = CustomDialog("", this)
        binding.ivUpRegister.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {

            binding.inputFullName.editText?.clearError()
            binding.inputEmail.editText?.clearError()
            binding.inputPassword.editText?.clearError()
            binding.inputPostalCode.editText?.clearError()
            binding.inputUF.editText?.clearError()
            binding.inputCity.editText?.clearError()

            if (binding.inputFullName.editText!!.isNullOrEmpty()) {
                binding.inputFullName?.editText?.setError(getString(R.string.blank_name))
                return@setOnClickListener
            } else if (!binding?.inputEmail?.editText?.isEmailValid()!!) {
                binding.inputEmail?.editText?.setError(getString(R.string.invalid_email))
                return@setOnClickListener
            } else if (binding.inputEmail.editText?.isNullOrEmpty()!!) {
                binding.inputEmail.editText?.setError((getString(R.string.blank_email)))
                return@setOnClickListener
            } else if (!binding.inputPassword.editText?.isPasswordValid()!!) {
                binding.inputPassword.editText?.setError(getString(R.string.password_min))
                return@setOnClickListener
            } else if (binding.inputPassword?.editText?.isNullOrEmpty()!!) {
                binding.inputPassword?.editText?.setError(getString(R.string.blank_pw))
                return@setOnClickListener
            } else if (!binding.inputBirthDate.editText?.isBirthDateValid()!!) {
                binding.inputBirthDate.editText?.setError(getString(R.string.birthdate_invalid))
            } else if (binding.inputBirthDate.editText?.isNullOrEmpty()!!) {
                binding.inputBirthDate.editText?.setError(getString(R.string.blank_date))
            } else if (!binding.inputPostalCode.editText?.isPostalCodeValid()!!) {
                binding.inputPostalCode.editText?.setError(getString(R.string.invalid_postal_code))
            } else if (binding.inputPostalCode.editText?.isNullOrEmpty()!!) {
                binding.inputPostalCode.editText?.setError(getString(R.string.blank_postal_code))
            } else if (!binding.inputUF.editText?.isUFValid()!!) {
                binding.inputUF.editText?.setError(getString(R.string.UF_invalid))
                return@setOnClickListener
            } else if (binding.inputUF.editText?.isNullOrEmpty()!!) {
                binding.inputUF.editText?.setError(getString(R.string.UF_blank))
                return@setOnClickListener
            } else if (binding.inputCity.editText?.isNullOrEmpty()!!) {
                binding.inputCity.editText?.setError(getString(R.string.blank_city))
            } else {
                var senUser = RegisterUser().apply {
                    this.fullName = binding.inputFullName.editText?.text.toString()
                    this.email = binding.inputEmail.editText?.text.toString()
                    this.birthDate = binding.inputBirthDate?.editText?.text.toString()
                    this.cep = binding.inputPostalCode.editText?.text.toString()
                    this.uf = binding.inputUF.editText?.text.toString()
                    this.city = binding.inputCity.editText?.text.toString()
                    this.lat = latitude
                    this.lng = longitute
                }
                sendForm(senUser)
            }
        }
        binding.inputBirthDate.editText?.addTextChangedListener(
            Mask.mask(
                "##/##/####",
                binding?.inputBirthDate?.editText!!
            )
        )
        binding.inputPostalCode.editText?.addTextChangedListener(
            Mask.mask(
                "#####-###",
                binding?.inputPostalCode?.editText!!
            )
        )
        binding.inputPostalCode.editText?.setOnFocusChangeListener { view, b -> fetchAddress(binding?.inputPostalCode?.editText!!) }
        binding.inputPostalCode.editText?.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                fetchAddress(binding?.inputPostalCode?.editText!!)
                true
            }else
                false

        }
    }

    private fun sendForm(sendUser: RegisterUser) {
        //register user
        viewmodel.registerUser(sendUser, binding.inputPassword.editText?.text.toString())
    }

    private fun fetchAddress(edtPostalCode: EditText) {
        val postalCode = Mask.removeMask(edtPostalCode.text.toString())
        if (postalCode.length == 8) {
            viewmodel.getAddress(postalCode)
        }
    }

    private fun showErrorDialog(): AlertDialog {
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                    })

                setMessage("CEP não encontrado.")
            }
            builder.create()
        }
        return alertDialog!!
    }

    private fun onRegisterError(errorMessage: String?) {
        if (!errorMessage.isNullOrEmpty()) {
            var alert = showErrorDialog(errorMessage)
            alert.show()
        }
    }

    private fun showErrorDialog(text: String): AlertDialog {
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                    })

                setMessage(text)
            }
            builder.create()
        }
        return alertDialog!!
    }
}
