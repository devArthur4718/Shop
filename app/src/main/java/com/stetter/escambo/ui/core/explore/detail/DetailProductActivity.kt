package com.stetter.escambo.ui.core.explore.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityProductBinding
import com.stetter.escambo.net.models.ProductByLocation

class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        getDataFromBundle()
        setViews()
    }

    private fun getDataFromBundle() {
        if (intent.hasExtra("product")) {
            val product = intent.getSerializableExtra("product") as? ProductByLocation
            setDataToViews(product)

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToViews(product: ProductByLocation?) {
        product?.let { data ->
            binding.tvProductTitle.text = data.product
            binding.tvItemDetailDescription.text = data.description
            binding.tvLocale.text = "${data.city}/${data.uf}"


        }

    }


    private fun setViews() {
        binding.ivCloseDetail.setOnClickListener {
            finish()
        }
    }
}
