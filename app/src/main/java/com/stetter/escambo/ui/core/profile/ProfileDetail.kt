package com.stetter.escambo.ui.core.profile

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityProfileDetailBinding
import com.stetter.escambo.extension.*
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.ui.base.BaseActivity
import com.stetter.escambo.ui.core.add.AddProduct
import com.stetter.escambo.ui.login.LoginActivity
import java.util.*


class ProfileDetail : BaseActivity() {

    private lateinit var binding: ActivityProfileDetailBinding
    private lateinit var viewmodel: UpdateProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_detail)
        viewmodel = ViewModelProvider(this)[UpdateProfileViewModel::class.java]
        setObservables()
    }

    private fun setObservables() {

        mainViewModel.getUserDataFromDatabase()
        mainViewModel.userProfileData.observe(this, Observer { onUserDataReceveid(it) })
        viewmodel.imagePickIntent.observe(this, Observer { onPickImageIntent(it) })
        viewmodel.onPhotoFileReceived.observe(this, Observer { onProfileImageReceived(it) })
        viewmodel.uploadSucess.observe(this, Observer { onUserProfildeUpdated(it) })

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
        viewmodel.updateUser(sendUser, binding.inputPassword.editText?.text.toString())
    }

    private fun onProfileImageReceived(imgUrl: String) {
        val storage = FirebaseStorage.getInstance()
        if (imgUrl.length > 1) {
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

    companion object {
        fun newInstance() = AddProduct()
        const val RQ_PICK_PHOTO = 0
        const val RQ_TAKE_PHOTO = 1
        const val STORAGE_URL = "gs://escambo-1b51d.appspot.com"

    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
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
                        val bitmapDrawable = BitmapDrawable(bitmap)
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
    private fun onUserDataReceveid(userdata: RegisterUser?) {
        //UpdateUI
        userdata?.let {
            binding.inputFullName.editText?.setText(it.fullName)
            binding.inputEmail.editText?.setText(it.email)
            binding.inputPassword.editText?.setText("******")
            userProfilePhoto = userdata.photoUrl
            binding.inputBirthDate.editText?.setText(it.birthDate)
            binding.inputPostalCode.editText?.setText(it.cep)
            binding.inputCity.editText?.setText(it.city)
            binding.inputUF.editText?.setText(it.uf)
        }

        //Load user photo
        val storage = FirebaseStorage.getInstance()
        if (userdata!!.photoUrl.length > 1) {
            val gsReference =
                storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com${userdata.photoUrl}")
            GlideApp.with(this)
                .load(gsReference)
                .placeholder(this?.CircularProgress())
                .into(binding.ivDetailProfileImage)

        } else {
            binding.ivDetailProfileImage.setImageDrawable(resources.getDrawable(R.drawable.ic_young))
        }
    }


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
}