package com.stetter.escambo.ui.register

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityRegisterBinding
import com.stetter.escambo.extension.Mask
import com.stetter.escambo.ui.dialog.LoadingDialog
import com.stetter.escambo.ui.dialogs.CustomDialog

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewmodel: RegisterViewModel
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var errorDialog: CustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        viewmodel = ViewModelProviders.of(this)[RegisterViewModel::class.java]
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

        viewmodel.addressValue.observe(this, Observer { response ->
            if (response != null) {
                binding.edtCity.setText(response.localidade)
                binding.edtUF.setText(response.uf)
            }
        })

        viewmodel.showErrorDialog.observe(this, Observer {
            if (it) {
                var alert =  showErrorDialog()
                alert.show()
            }

        })

    }


    private fun initViews() {
        loadingDialog = LoadingDialog(this)
        errorDialog = CustomDialog("", this)
        binding.ivUpRegister.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            //TODO: VALIDATE FORM INPUTS

        }

        binding.edtPostalCode.addTextChangedListener(Mask.mask("#####-###", binding.edtPostalCode))
        binding.edtPostalCode.setOnFocusChangeListener { view, b -> fetchAddress(binding.edtPostalCode) }
    }

    private fun fetchAddress(edtPostalCode: EditText) {
        if (Mask.removeMask(edtPostalCode.text.toString()).length == 8) {
            viewmodel.getAddress(edtPostalCode.text.toString())
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

                setMessage("CEP não encontrato")
            }
            builder.create()
        }

        return alertDialog!!

    }

}
