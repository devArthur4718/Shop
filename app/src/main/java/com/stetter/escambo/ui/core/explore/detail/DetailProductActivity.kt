package com.stetter.escambo.ui.core.explore.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityProductBinding

class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setViews()
    }

    private fun setViews() {
        binding.ivCloseDetail.setOnClickListener {
            finish()
        }
    }
}
