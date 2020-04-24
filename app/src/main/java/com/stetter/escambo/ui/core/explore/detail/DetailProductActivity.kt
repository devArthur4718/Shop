package com.stetter.escambo.ui.core.explore.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityProductBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.*
import com.stetter.escambo.ui.adapter.ProductPhotoAdapter
import com.stetter.escambo.ui.base.BaseActivity
import com.stetter.escambo.ui.core.profile.OtherUser
import kotlinx.android.synthetic.main.activity_product.*

class DetailProductActivity : BaseActivity() {

    private lateinit var userData: RegisterUser
    private lateinit var binding: ActivityProductBinding
    private lateinit var adapter : ProductPhotoAdapter
    private lateinit var viewmodel : DetailProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        viewmodel = ViewModelProvider(this)[DetailProductViewModel::class.java]

        setObservables()
        getDataFromBundle()
        setViews()
    }

    private fun setObservables() {
        viewmodel.querryProgress.observe(this, Observer { onUserDataReceived(it) })
        viewmodel.interestRequest.observe(this, Observer { onSendInterestResult(it) })
    }

    private fun onSendInterestResult(productKey: String?) {
        productKey?.let {

                mainViewModel.updateUserInterestList(it)
        }

    }


    private fun onUserDataReceived(user: RegisterUser?) {
        user?.let {item ->
            userData = item
            var intent = Intent(this, OtherUser::class.java)
            intent.putExtra("user", item)
            startActivity(intent)
        }
    }


    private fun getDataFromBundle() {
        if (intent.hasExtra("product")) {
            val product = intent.getSerializableExtra("product") as? ProductByLocation
            var fetchedProduct = Product().apply {
                this.uid = product!!.uid
                this.productUrl = product.productUrl
                this.product = product.product
                this.description = product.description
                this.category = product.category
                this.value = product.value
                this.datePosted = product.datePosted
                this.username = product.username
                this.userPhoto = product.userPhoto
                this.lat = product.lat
                this.lng = product.lng
                this.uf = product.uf
                this.city = product.city
                this.productKey = product.productKey
            }
            setDataToViews(fetchedProduct)
        }else if(intent.hasExtra("productList")){
            val product = intent.getSerializableExtra("productList") as? Product
            setDataToViews(product)
        }
    }

    private fun setDataToViews(product: Product?) {
        val storage = FirebaseStorage.getInstance()
        product?.let { data ->
            binding.tvProductTitle.text = data.product
            binding.tvItemDetailDescription.text = data.description
            binding.tvLocale.text = "${data.city}/${data.uf}"
            binding.tvDetailAuthor.text = data.username
            binding.tvProductDetailValue.text = data.value

            if(data.userPhoto.length > 0) {
                val gsReferencePhoto = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${data.userPhoto}")
                GlideApp.with(this)
                    .asDrawable()
                    .load(gsReferencePhoto)
                    .placeholder(CircularProgress())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.ivUserDetail)
            }
            binding.ivUserDetail.setOnClickListener {
                //Fetch user and then open an intent
                viewmodel.retrieveUserInformation(data.uid)
            }
            binding.btnOpenUserDetail.setOnClickListener {
                ivUserDetail.performClick()
            }



            binding.btnInterestInExhange.visibility = if(mainViewModel.currentUserUID() != data.uid) View.VISIBLE else View.GONE
            when {
                binding.btnInterestInExhange.visibility == View.VISIBLE -> {
                    binding.btnInterestInExhange.setOnClickListener {
                        sendProductInterest(product)
                    }

                }
            }



            var adapter = ProductPhotoAdapter(ProductPhotoAdapter.PhotoListener {

            })
            adapter.data = data.productUrl
            binding.rvProductImages.adapter = adapter
        }
    }

    private fun sendProductInterest(product: Product) {
        mainViewModel.updateCurrentID()

        val interest = ProductInterest(
            product.product,
            product.productUrl[0],
            product.productKey,
            product.uid
        )

        viewmodel.sendProductInterest(interest)

    }


    private fun setViews() {
        binding.ivCloseDetail.setOnClickListener {
            finish()
        }
    }
}
