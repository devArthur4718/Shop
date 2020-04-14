package com.stetter.escambo.ui.core.explore.filter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityFilterBinding
import com.stetter.escambo.extension.hideKeyBoard
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.ui.adapter.RecentProductAdapter

class FilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterBinding
    private lateinit var viewmodel: FilterViewModel
    private val searchProductAdapter by lazy { RecentProductAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)
        viewmodel = ViewModelProvider(this)[FilterViewModel::class.java]
        setAdapters()
        setObservables()
        initView()

    }
    private fun setObservables() {
        viewmodel.querryByName.observe(this, Observer { onSearchProductResponse(it) })
        viewmodel.loadingProgress.observe(this, Observer { onLoading(it) })
    }

    var productNameSearch = ""
    private fun initView() {
        binding.btnCloseFilter.setOnClickListener {
            finish()
        }
        binding.edSearchItem.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyBoard(v)
                productNameSearch = binding.edSearchItem.text.toString()
                viewmodel.searchProduct()
                true
            } else
                false

        }
    }

    private fun onLoading(loading: Boolean?) = loading?.let { loading ->
        binding.pbQuerry.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun setAdapters() {
        binding.rvQuerrySearchResult.adapter = searchProductAdapter
    }

    private fun onSearchProductResponse(data: ArrayList<Product>) {
        viewmodel.hideProgress()
        if (data.isEmpty()) {
            //no ITEM found
            // prompt user
            binding.rvQuerrySearchResult.visibility = View.GONE
            binding.gpNoItems.visibility = View.VISIBLE
        } else {
            binding.rvQuerrySearchResult.visibility = View.VISIBLE
            binding.gpNoItems.visibility = View.GONE
            searchProductAdapter.data =  data.filter { it.product.toLowerCase().contains(productNameSearch) }

        }
    }

}
