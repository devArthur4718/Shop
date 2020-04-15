package com.stetter.escambo.ui.core.explore.filter

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
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
import com.stetter.escambo.net.retrofit.responses.UfsResponseItem
import com.stetter.escambo.ui.adapter.RecentProductAdapter
import com.stetter.escambo.ui.base.BaseActivity

class FilterActivity : BaseActivity() {

    private lateinit var binding: ActivityFilterBinding
    private lateinit var viewmodel: FilterViewModel
    private val searchProductAdapter by lazy { RecentProductAdapter() }
    private lateinit var adapterSpinner: ArrayAdapter<String>
    private lateinit var adapterUfs: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)
        viewmodel = ViewModelProvider(this)[FilterViewModel::class.java]
        setAdapters()
        setObservables()
        initView()

    }
    private fun setObservables() {
        viewmodel.fetchCategories()
        viewmodel.fetchUFsIds()
        mainViewModel.retrieveUserLocation()
        viewmodel.listCategoryList.observe( this,  Observer { onConfigureCategoryAdapter(it) })
        viewmodel.querryByName.observe(this, Observer { onSearchProductResponse(it) })
        viewmodel.queryByValues.observe(this, Observer { onSearchValuesResponse(it) })
        viewmodel.querryCategories.observe(this, Observer { onSearchByCategories(it) })
        viewmodel.listUfs.observe(this, Observer { onUfsListReceived(it) })
        viewmodel.loadingProgress.observe(this, Observer { onLoading(it) })
    }

    private fun onUfsListReceived(ufsList: List<UfsResponseItem>?) {
        var ufString  = ArrayList<String>()
        ufsList?.forEach {
            ufString.add(it.nome)
        }
        adapterUfs = ArrayAdapter(this, android.R.layout.simple_list_item_1, ufString)
        binding.btnLocalization.setOnClickListener { showFilterLocalization(viewmodel,adapterUfs,ufsList) }
    }


    private fun onConfigureCategoryAdapter(categoryList: List<String>) {
        adapterSpinner = ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList)
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
        binding.btnCategory.setOnClickListener { showFilterCategory(viewmodel,adapterSpinner) }

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

    private fun onSearchByCategories(categoryList: ArrayList<Product>) {
        viewmodel.hideProgress()
        if(categoryList.isEmpty()){
            //no ITEM found
            // prompt user
            toggleViews(true)
        }else{
            toggleViews(false)
            searchProductAdapter.data = categoryList
        }

    }


    private fun toggleViews(isEmpty : Boolean){
        binding.rvQuerrySearchResult.visibility = if(isEmpty) View.GONE else View.VISIBLE
        binding.gpNoItems.visibility = if(isEmpty) View.VISIBLE else View.GONE
    }
}
