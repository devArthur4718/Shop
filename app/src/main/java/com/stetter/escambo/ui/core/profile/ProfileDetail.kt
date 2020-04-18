package com.stetter.escambo.ui.core.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityProfileDetailBinding
import com.stetter.escambo.extension.*
import com.stetter.escambo.extension.dialogs.showPickImageProfile
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.net.retrofit.responses.postalResponse
import com.stetter.escambo.ui.base.BaseActivity
import com.stetter.escambo.ui.core.add.AddProduct
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ProfileDetail : BaseActivity() {

    private lateinit var binding: ActivityProfileDetailBinding
    private lateinit var viewmodel: UpdateProfileViewModel
    var latitude  = 0.0
    var longitute = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_detail)
        viewmodel = ViewModelProvider(this)[UpdateProfileViewModel::class.java]
        getFromBundle()
        initViews()
        setObservables()
    }

    var productCount = 0
    lateinit var currentUser : RegisterUser
    private fun getFromBundle() {
        if(intent.hasExtra("productCount")){
            productCount = intent.getIntExtra("productCount", 0)
        }
        if(intent.hasExtra("currentUser")){
            currentUser = intent.getSerializableExtra("currentUser") as RegisterUser

        }
    }

    var userKey = ""
    private fun initViews() {
        currentUser?.let {
            binding.inputFullName.editText?.setText(it.fullName)
            binding.inputEmail.editText?.setText(it.email)
            binding.inputPassword.editText?.setText("******")
            userProfilePhoto = it.photoUrl
            binding.inputBirthDate.editText?.setText(it.birthDate)
            binding.inputPostalCode.editText?.setText(it.cep)
            binding.inputCity.editText?.setText(it.city)
            binding.inputUF.editText?.setText(it.uf)
            productList = it.productsList as ArrayList<String>
            userKey = it.clientID

            val storage = FirebaseStorage.getInstance()
            if(it.photoUrl.length > 1){
                val gsReference =
                    storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${it.photoUrl}")
                GlideApp.with(this)
                    .load(gsReference)
                    .placeholder(this?.CircularProgress())
                    .into(binding.ivDetailProfileImage)
            }else{
                binding.ivDetailProfileImage.setImageDrawable(resources.getDrawable(R.drawable.ic_young))
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
    }

    private fun fetchAddress(editText: EditText) {

        val postalCode = Mask.removeMask(binding.inputPostalCode.editText!!.text.toString())
        if (postalCode.length == 8) {
            viewmodel.getAddress(postalCode)
        }
    }

    companion object {
        const val RQ_PICK_PHOTO = 0
        const val RQ_TAKE_PHOTO = 1
    }

    private fun setObservables() {

//        mainViewModel.getUserDataFromDatabase()
//        mainViewModel.userProfileData.observe(this, Observer { onUserDataReceveid(it) })
        viewmodel.imagePickIntent.observe(this, Observer { onPickImageIntent(it) })
        viewmodel.onPhotoFileReceived.observe(this, Observer { onProfileImageReceived(it) })
        viewmodel.uploadSucess.observe(this, Observer { onUserProfildeUpdated(it) })
        viewmodel.addressValue.observe(this, Observer { response -> onAddressReceived(response) })

        viewmodel.showErrorDialog.observe(this, Observer {
            if (it) {
                var alert = showErrorDialog()
                alert.show()
            }

        })

        binding.ivLogout.setOnClickListener {
            viewmodel.logout()
            setResult(Activity.RESULT_OK)
            finish()
        }


        binding.tvCloseProfileDetail.setOnClickListener {
            finish()
        }



        binding.ivChangeProfilePhoto.setOnClickListener {
            //Update photo
            showPickImageProfile(viewmodel)
        }

        binding.btnUpdateProfile.setOnClickListener {
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
                var sendUser = RegisterUser().apply {
                    this.fullName = binding.inputFullName.editText?.text.toString()
                    this.email = binding.inputEmail.editText?.text.toString()
                    this.birthDate = binding.inputBirthDate?.editText?.text.toString()
                    this.cep = binding.inputPostalCode.editText?.text.toString()
                    this.uf = binding.inputUF.editText?.text.toString()
                    this.city = binding.inputCity.editText?.text.toString()
                    this.photoUrl = userProfilePhoto
                    this.lng = GeocoderLocation(binding.inputUF.editText?.text.toString()).first
                    this.lat = GeocoderLocation(binding.inputUF.editText?.text.toString()).second
                    this.products = productCount
                    this.productsList = productList
                    this.clientID = viewmodel.getClientID()
                }
                updateUser(sendUser)
            }
        }
    }

    private fun onUserProfildeUpdated(updated: Boolean?) {
        updated?.let { updated -> if (updated) finish() }
    }

    private fun updateUser(sendUser: RegisterUser) {
        //register user
        viewmodel.updateCurrentUser(sendUser)
    }

    var profileImage = ""
    private fun onProfileImageReceived(imgUrl: String) {
        val storage = FirebaseStorage.getInstance()
        if (imgUrl.length > 1) {
            profileImage = imgUrl
            userProfilePhoto = imgUrl
            val gsReference =
                storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${imgUrl}")
            GlideApp.with(this)
                .load(gsReference)
                .placeholder(this?.CircularProgress())
                .into(binding.ivDetailProfileImage)

        } else {
            binding.ivDetailProfileImage.setImageDrawable(resources.getDrawable(R.drawable.ic_young))
        }
    }

    private fun onPickImageIntent(it: Boolean?) {
        it?.let {
            if (it) {
                pickImage()
            }
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, AddProduct.RQ_PICK_FROM_GALLERY)
    }


    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            //TODO : Fetch data from camera
            RQ_PICK_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        viewmodel.onPickPhotoSuccess()
                        viewmodel.closePhotoIntent()
                        selectedPhotoUri = data.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            this?.contentResolver,
                            selectedPhotoUri
                        )
                        sendImageToFirebase(selectedPhotoUri)
                    }
                }
            }
        }

    }

    private fun sendImageToFirebase(selectedPhotoUri: Uri?) {
        val filename = UUID.randomUUID().toString()
        selectedPhotoUri?.let { uri ->
            viewmodel.uploadImageToFirebase(filename, uri)
        }

    }

    var userProfilePhoto = ""
    var productList = ArrayList<String>()


    fun onEditClick(view: View) {
        when (view.id) {
            R.id.tvEditFullName -> toogleInput(binding.inputFullName, binding.tvEditFullName)
            R.id.tvEditEmail -> toogleInput(binding.inputEmail, binding.tvEditEmail)
            R.id.tvEditPassword -> toogleInput(binding.inputPassword, binding.tvEditPassword)
            R.id.tvEditBirthDate -> toogleInput(binding.inputBirthDate, binding.tvEditBirthDate)
            R.id.tvEditPostalCode -> toogleInput(binding.inputPostalCode, binding.tvEditPostalCode)
            R.id.tvEditUF -> toogleInput(binding.inputUF, binding.tvEditUF)
            R.id.tvEditCity -> toogleInput(binding.inputCity, binding.tvEditCity)
        }
    }

    fun toogleInput(view: TextInputLayout, textEditLabel: TextView) {
        view.editText!!.isEnabled = !view.editText!!.isEnabled
        view.editText!!.requestFocus()
        view.editText!!.setSelection(view.editText!!.text.toString().length)
        if (view.editText!!.isEnabled) {
            showKeyboard()
            textEditLabel.text = getString(R.string.save_it)
        } else {
            textEditLabel.text = getString(R.string.edit)
            hideKeyBoard(view)
        }
    }

    private fun onAddressReceived(response: postalResponse) {
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
}
