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
import com.stetter.escambo.extension.dialogs.showFilterCategory
import com.stetter.escambo.extension.dialogs.showFilterLocalization
import com.stetter.escambo.extension.hideKeyBoard
import com.stetter.escambo.extension.dialogs.showFilterValue
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.ui.adapter.RecentProductAdapter
import com.stetter.escambo.ui.base.BaseActivity

class FilterActivity : BaseActivity() {

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
        mainViewModel.retrieveUserLocation()
        viewmodel.querryByName.observe(this, Observer { onSearchProductResponse(it) })
        viewmodel.queryByValues.observe(this, Observer { onSearchValuesResponse(it) })
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

        binding.btnValue.setOnClickListener {   showFilterValue(viewmodel)  }
        binding.btnCategory.setOnClickListener { showFilterCategory(viewmodel) }
        binding.btnLocalization.setOnClickListener { showFilterLocalization(viewmodel) }

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
            toggleViews(true)

        } else {
            toggleViews(false)
            searchProductAdapter.data =  data.filter { it.product.toLowerCase().contains(productNameSearch) }

        }
    }

    private fun onSearchValuesResponse(productList: ArrayList<Product>) {
        viewmodel.hideProgress()
        if(productList.isEmpty()){
            //no ITEM found
            // prompt user
            toggleViews(true)
        }else{
            toggleViews(false)
            searchProductAdapter.data = productList
        }

    }

    private fun toggleViews(isEmpty : Boolean){
        binding.rvQuerrySearchResult.visibility = if(isEmpty) View.GONE else View.VISIBLE
        binding.gpNoItems.visibility = if(isEmpty) View.VISIBLE else View.GONE
    }
}
