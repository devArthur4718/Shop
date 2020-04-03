package com.stetter.escambo.ui.core.explore.filter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityFilterBinding

class FilterActivity : AppCompatActivity() {

    private lateinit var binding :  ActivityFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)
        setObservables()

    }

    private fun setObservables() {
        binding.btnCloseFilter.setOnClickListener {
            finish()
        }
    }
}
