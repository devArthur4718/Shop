package com.stetter.escambo.ui.core.profile

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityOtherUserBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.ui.base.BaseActivity
import java.lang.IllegalArgumentException

class OtherUser : BaseActivity() {
    private lateinit var binding: ActivityOtherUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_user)
        intentBundle()

    }

    private fun intentBundle() {
        if (intent.hasExtra("user")) {
            var user = intent.getSerializableExtra("user") as? RegisterUser
            setDataToViews(user)
        }
    }

    private fun setDataToViews(user: RegisterUser?) {
        user?.let {
            binding.tvLoggedUserProfile.text = user.fullName
            binding.tvLoggedUserLocation.text = user.city + "/" + user.uf
            binding.tvUserMatches.text = "${user.matches} Matches"
            val storage = FirebaseStorage.getInstance()
            if (user.photoUrl.length > 1) {
                try {
                    val gsReference =
                        storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${it.photoUrl}")
                    GlideApp.with(this)
                        .load(gsReference)
                        .placeholder(CircularProgress())
                        .into(binding.ivProfileImage)

                } catch (e: IllegalArgumentException) {
                    Log.e("PROFILE", "Error : $e")
                }
            } else {
                binding.ivProfileImage.setImageDrawable(resources.getDrawable(R.drawable.ic_young))
            }

        }

        binding.ivCloseOtherDetail.setOnClickListener {   finish() }



    }

}


