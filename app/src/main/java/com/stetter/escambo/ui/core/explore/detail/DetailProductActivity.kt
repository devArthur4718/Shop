package com.stetter.escambo.ui.core.explore.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityProductBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.ProductByLocation
import com.stetter.escambo.ui.adapter.ProductPhotoAdapter
import com.stetter.escambo.ui.base.BaseActivity
import java.lang.Exception
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DetailProductActivity : BaseActivity() {

    private lateinit var binding: ActivityProductBinding
    private val adapter by lazy { ProductPhotoAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        setAdapters()
        getDataFromBundle()
        setViews()
    }

    private fun setAdapters() {
        binding.rvProductImages.adapter = adapter
    }

    private fun getDataFromBundle() {
        if (intent.hasExtra("product")) {
            val product = intent.getSerializableExtra("product") as? ProductByLocation
            setDataToViews(product)
        }else if(intent.hasExtra("productList")){
            val product = intent.getSerializableExtra("productList") as? Product
            setDataToViews(product)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setDataToViews(product: ProductByLocation?) {
        val storage = FirebaseStorage.getInstance()
        product?.let { data ->
            binding.tvProductTitle.text = data.product
            binding.tvItemDetailDescription.text = data.description
            binding.tvLocale.text = "${data.city}/${data.uf}"
            binding.tvDetailAuthor.text = data.username

            var moneytext = data.value.toString().replaceRange(data.value.toString().length  -2, data.value.toString().length, "")

            try{
                var symbols = DecimalFormatSymbols()
                symbols.decimalSeparator = ','
                var moneyFormat = DecimalFormat("R$ ###,###,###,###", symbols)
                binding.tvProductDetailValue.text = moneyFormat.format(moneytext.toDouble()).toString().replace(".", ",")
            }catch (e : Exception){
                Log.d("ProductAdapter", "Error: $e")
            }

            if(data.userPhoto.length > 0) {
                val gsReferencePhoto = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${data.userPhoto}")
                GlideApp.with(this)
                    .asDrawable()
                    .load(gsReferencePhoto)
                    .placeholder(CircularProgress())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.ivUserDetail)
            }

            adapter.data = data.productUrl
        }
    }

    private fun setDataToViews(product: Product?) {
        val storage = FirebaseStorage.getInstance()
        product?.let { data ->
            binding.tvProductTitle.text = data.product
            binding.tvItemDetailDescription.text = data.description
            binding.tvLocale.text = "${data.city}/${data.uf}"
            binding.tvDetailAuthor.text = data.username

            var moneytext = data.value.toString().replaceRange(data.value.toString().length  -2, data.value.toString().length, "")

            try{
                var symbols = DecimalFormatSymbols()
                symbols.decimalSeparator = ','
                var moneyFormat = DecimalFormat("R$ ###,###,###,###", symbols)
                binding.tvProductDetailValue.text = moneyFormat.format(moneytext.toDouble()).toString().replace(".", ",")
            }catch (e : Exception){
                Log.d("ProductAdapter", "Error: $e")
            }

            if(data.userPhoto.length > 0) {
                val gsReferencePhoto = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${data.userPhoto}")
                GlideApp.with(this)
                    .asDrawable()
                    .load(gsReferencePhoto)
                    .placeholder(CircularProgress())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.ivUserDetail)
            }

            adapter.data = data.productUrl
        }
    }


    private fun setViews() {
        binding.ivCloseDetail.setOnClickListener {
            finish()
        }
    }
}
