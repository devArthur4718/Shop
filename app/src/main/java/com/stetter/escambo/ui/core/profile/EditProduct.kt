package com.stetter.escambo.ui.core.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ActivityEditProductBinding
import com.stetter.escambo.extension.Mask
import com.stetter.escambo.extension.getTimeStamp
import com.stetter.escambo.extension.watcher.MoneyTextWatcher
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.ui.adapter.EditProductAdapter
import java.util.*

class EditProduct : AppCompatActivity() {

    lateinit var currentUser: RegisterUser
    lateinit var item: Product
    private lateinit var binding : ActivityEditProductBinding
    private lateinit var viewmodel : EditProductViewModel
    private lateinit var adapterSpinner: ArrayAdapter<String>
    private val adapterEdit by lazy { EditProductAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_product)
        viewmodel = ViewModelProvider(this)[EditProductViewModel::class.java]
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
        viewmodel.onDeletedProduct.observe(this, Observer { onDeletedProduct(it) })
        viewmodel.onProductUpdate.observe(this, Observer { onProductUpdate(it) })

        binding.btnCloseEdit.setOnClickListener {
            finish()
        }

        binding.edtItemValue.addTextChangedListener(MoneyTextWatcher(binding.edtItemValue, Locale("pt", "BR")))

        binding.btnPublishItem.setOnClickListener {

            val uid = viewmodel.getUid()
            var category = binding.spCategory.selectedItem.toString()

            val product = Product(
                uid,
                item.productUrl,
                binding.edtItemName.text.toString(),
                binding.edtItemDescription.text.toString(),
                category,
                binding.edtItemValue.text.toString(),
                Calendar.getInstance().getTimeStamp(),
                currentUser.fullName,
                currentUser.photoUrl,
                currentUser.lat,
                currentUser.lng,
                currentUser.uf,
                currentUser.city,
                item.productKey
            )

            viewmodel.updateProduct(product)

        }

        binding.btnRemoveProduct.setOnClickListener {
            viewmodel.deleteProduct(item.productKey)
            var count = currentUser.products - 1
            viewmodel.updateProductCount(count)
            //TODO: Update Product Count
        }

    }

    private fun onDeletedProduct(status: Boolean?) {
        status?.let {
            if(status){
                //Product  deleted
                Toast.makeText(this, "Produto deletado",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Erro ao remover produto",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun onProductUpdate(status: Boolean?) {
        status?.let {
            if(status){
                Toast.makeText(this, "Produto Atualizado",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Erro ao atualizar produto",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    private fun onCategeroListReceived(categoryList: List<String>) {
        adapterSpinner = ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryList)
        binding.spCategory.adapter = adapterSpinner
    }

    private fun getDataFrombundle() {
        if(intent.hasExtra("productItem")){
            item = intent.getSerializableExtra("productItem") as Product
            setDataToview(item)
        }
        if(intent.hasExtra("user")){
             currentUser = intent.getSerializableExtra("user") as RegisterUser
        }
    }

    private fun setDataToview(item: Product) {
        binding.edtItemName.setText(item.product)
        binding.edtItemDescription.setText(item.description)
        //Set adapter itens
        binding.edtItemValue.setText(item.value)
        adapterEdit.data = item.productUrl
    }
}
