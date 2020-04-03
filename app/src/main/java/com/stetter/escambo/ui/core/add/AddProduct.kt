package com.stetter.escambo.ui.core.add

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.stetter.escambo.R
import com.stetter.escambo.databinding.AddProductFragmenetBinding

class AddProduct : Fragment() {

    companion object {
        fun newInstance() = AddProduct()
    }

    private lateinit var viewModel: AddProductViewModel
    private lateinit var binding : AddProductFragmenetBinding
    private lateinit var adapterSpinner  : ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_product_fragmenet,
            container,
            false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddProductViewModel::class.java)
        setObserbales()
    }

    private fun setObserbales() {
        viewModel.listCategorList.observe(viewLifecycleOwner, Observer { onConfigureCategoryAdapter(it) })
    }

    private fun onConfigureCategoryAdapter(categoryList: List<String>) {
        adapterSpinner = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, categoryList)
        binding.spCategory.adapter = adapterSpinner
    }

}
