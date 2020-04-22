package com.stetter.escambo.ui.core.explore.detail

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityFullSizedImageBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp

class FullSizedImage : AppCompatActivity() {

    private lateinit var binding : ActivityFullSizedImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_sized_image)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_sized_image)
        if(intent.hasExtra("itemUrl")){
            var item = intent.getStringExtra("itemUrl")

            val storage = FirebaseStorage.getInstance()
            try{
                val gsReference = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${item}")
                GlideApp.with(this)
                    .load(gsReference)
                    .placeholder(CircularProgress())
                    .into(binding.imageView7)

            }catch (e : IllegalArgumentException){
                Log.e("UserAdapter", "Error : $e")
            }
        }
    }
}
