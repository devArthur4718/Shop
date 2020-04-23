package com.stetter.escambo.ui.core.profile

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityOtherUserBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.ui.adapter.MyProductAdapter
import com.stetter.escambo.ui.base.BaseActivity
import com.stetter.escambo.utils.AppConstants
import java.lang.IllegalArgumentException

class OtherUser : BaseActivity() {
    private lateinit var user: RegisterUser
    private lateinit var binding: ActivityOtherUserBinding
    private lateinit var viewmodel : OtherUserViewModel
    private lateinit var adapter : MyProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_user)
        viewmodel = ViewModelProvider(this)[OtherUserViewModel::class.java]
        intentBundle()
    }

    private fun intentBundle() {
        if (intent.hasExtra("user")) {
            user = intent.getSerializableExtra("user") as RegisterUser
            setDataToViews(user)
            setObservables()
            setAdapters()
        }
    }

    private fun setObservables() {
        viewmodel.querryFirebase.observe(this, Observer { onUserProductsReceived(it) })

    }

    private fun setAdapters() {
        adapter = MyProductAdapter(MyProductAdapter.ProductListener {
            //Todo start activity intent to edit ou delete product
        })
        adapter.type = AppConstants.TYPE_EDIT_BLOCKED
        binding.rvRecentPosts.adapter = adapter
    }


    private fun onUserProductsReceived(datalist: ArrayList<Product>) {
        if(datalist.isEmpty()){
            //No Products
        }else{
            adapter.data = datalist
            binding.tvProdutos.text ="${datalist.size} Produtos"
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

            viewmodel.retrieveUserProducts(user.clientID)
        }

        binding.ivCloseOtherDetail.setOnClickListener {   finish() }
    }

}


