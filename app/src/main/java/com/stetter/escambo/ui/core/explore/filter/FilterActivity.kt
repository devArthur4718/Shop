package com.stetter.escambo.ui.core.explore.filter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityFilterBinding
import com.stetter.escambo.extension.dialogs.showFilterCategory
import com.stetter.escambo.extension.dialogs.showFilterLocalization
import com.stetter.escambo.extension.dialogs.showFilterValue
import com.stetter.escambo.extension.hideKeyBoard
import com.stetter.escambo.extension.isGPsEnabled
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.ProductByLocation
import com.stetter.escambo.net.retrofit.responses.UfsResponseItem
import com.stetter.escambo.ui.adapter.RecentProductAdapter
import com.stetter.escambo.ui.base.BaseActivity
import com.stetter.escambo.ui.core.CoreActivity

@Suppress("UNCHECKED_CAST")
class FilterActivity : BaseActivity() {

    private lateinit var binding: ActivityFilterBinding
    private lateinit var viewmodel: FilterViewModel
    private val searchProductAdapter by lazy { RecentProductAdapter() }
    private lateinit var adapterSpinner: ArrayAdapter<String>
    private lateinit var adapterUfs: ArrayAdapter<String>
    lateinit var fusedLocationClient: FusedLocationProviderClient
    var currentLat: Double? = null
    var currentLng: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)
        viewmodel = ViewModelProvider(this)[FilterViewModel::class.java]
        setAdapters()
        setObservables()
        initView()

    }
    private fun setObservables() {
        retrieveLocation()
        viewmodel.fetchCategories()
        viewmodel.fetchUFsIds()
        viewmodel.listCategoryList.observe( this,  Observer { onConfigureCategoryAdapter(it) })
        viewmodel.querryByName.observe(this, Observer { onSearchProductResponse(it) })
        viewmodel.queryByValues.observe(this, Observer { onSearchValuesResponse(it) })
        viewmodel.querryCategories.observe(this, Observer { onSearchByCategories(it) })
        viewmodel.querryByLocation.observe(this, Observer { onSearchByLocation(it) })
        viewmodel.querryByUf.observe(this, Observer { onSearchByUf(it) })
        viewmodel.listUfs.observe(this, Observer { onUfsListReceived(it) })
        viewmodel.loadingProgress.observe(this, Observer { onLoading(it) })
    }


    //User location used to retrieve products next to him
    private fun retrieveLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.\
                if(location == null){
                    //Check enabled, if not ,call again
                    if(!isGPsEnabled()){
                        //Prompt user.
                        showDialog("GPS desativado. Alguns serviços não funcionaram corretamente! Deseja ativar?").show()
                    }else{
                        retrieveLocation()
                    }

                }else{
                    //Call update lists in fragment
                    currentLat = location.latitude
                    currentLng = location.longitude
                }
            }.addOnFailureListener { error ->
                Log.e("Explore", "Error: $error")
            }
    }

    private fun showDialog(text: String): AlertDialog {
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialog, id ->
                        var callLocationSetting = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivityForResult(callLocationSetting, CoreActivity.RQ_GPS_SETTINGS)
                        dialog.dismiss()
                    })
                setNegativeButton(
                    "Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })

                setMessage(text)
            }
            builder.create()
        }
        return alertDialog!!
    }


    private fun onUfsListReceived(ufsList: List<UfsResponseItem>?) {
        var ufString  = ArrayList<String>()
        ufsList?.forEach {
            ufString.add(it.nome)
        }
        adapterUfs = ArrayAdapter(this, android.R.layout.simple_list_item_1, ufString)
        var blank = listOf<String>("")
        binding.btnLocalization.setOnClickListener { showFilterLocalization(viewmodel, this,adapterUfs,ufsList, currentLat, currentLng) }
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
                viewmodel.searchByProductName(productNameSearch)
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
            //TODO: Filter min and max using firestore querry
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

    private fun onSearchByLocation(productLocationList: ArrayList<ProductByLocation>) {
        viewmodel.hideProgress()
        if(productLocationList.isEmpty()){
            //no ITEM found
            // prompt user
            toggleViews(true)
        }else{
            toggleViews(false)
            var productsList = ArrayList<Product>()
            var filteredProducts = productLocationList.filter {it.uid != viewmodel.retrieveUserUID()  }
            filteredProducts.forEach {
                it.distance = it.distanceBetween(currentLat, currentLng, it.lat, it.lng )
            }

            filteredProducts.sortedBy { it.distance }.forEach {

                    productsList.add(Product(it.uid,
                        it.productUrl,
                        it.product,
                        it.description,
                        it.category,
                        it.value,
                        it.datePosted,
                        it.username,
                        it.userPhoto,
                        it.lat,
                        it.lng,
                        it.uf,
                        it.city))

            }

            searchProductAdapter.data = productsList
        }
    }

    private fun onSearchByUf(productList: ArrayList<ProductByLocation>) {
        viewmodel.hideProgress()
        if(productList.isEmpty()){
            //no ITEM found
            // prompt user
            toggleViews(true)
        }else {

            viewmodel.hideProgress()
            toggleViews(false)
            var ufProductList = ArrayList<Product>()
            productList.forEach {
                ufProductList.add(Product(it.uid,
                    it.productUrl,
                    it.product,
                    it.description,
                    it.category,
                    it.value,
                    it.datePosted,
                    it.username,
                    it.userPhoto,
                    it.lat,
                    it.lng,
                    it.uf,
                    it.city))

            }
            searchProductAdapter.data = ufProductList.filter { it.city.toLowerCase().contains(viewmodel.city.toLowerCase()) }

        }

    }

    private fun toggleViews(isEmpty : Boolean){
        binding.rvQuerrySearchResult.visibility = if(isEmpty) View.GONE else View.VISIBLE
        binding.gpNoItems.visibility = if(isEmpty) View.VISIBLE else View.GONE
    }
}
