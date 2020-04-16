package com.stetter.escambo.ui.core.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityEditProductBinding
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.ui.adapter.EditProductAdapter
import com.stetter.escambo.ui.adapter.UploadItemAdapter

class EditProduct : AppCompatActivity() {

    private lateinit var binding : ActivityEditProductBinding
    private lateinit var viewmodel : EditProfileViewmodel
    private lateinit var adapterSpinner: ArrayAdapter<String>
    private val adapterEdit by lazy { EditProductAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_product)
        viewmodel = ViewModelProvider(this)[EditProfileViewmodel::class.java]
        setAdapters()
        setObservables()
        getDataFrombundle()


    }

    private fun setAdapters() {
        binding.rvChangePhoto.adapter = adapterEdit
    }

    private fun setObservables() {
        viewmodel.fetchProductCategories()
        viewmodel.listCategoryList.observe(this, Observer { onCategeroListReceived(it) })

        binding.btnCloseEdit.setOnClickListener {
            finish()
        }
    }

    private fun onCategeroListReceived(categoryList: List<String>) {
        adapterSpinner = ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList)
        binding.spCategory.adapter = adapterSpinner
    }

    private fun getDataFrombundle() {
        if(intent.hasExtra("productItem")){
            var item = intent.getSerializableExtra("productItem") as Product
            setDataToview(item)
            Log.d("sucesss", "item")
        }
    }

    var imageUrls = ArrayList<String>()

    private fun setDataToview(item: Product) {
        binding.edtItemName.setText(item.product)
        binding.edtItemDescription.setText(item.description)
        //Set adapter itens
        binding.edtItemValue.setText(item.value.toString())

        adapterEdit.data = item.productUrl

    }
}
