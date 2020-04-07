package com.stetter.escambo.ui.core.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.stetter.escambo.R
import com.stetter.escambo.databinding.ProfileFragmentBinding
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.ui.adapter.ItemProductAdapter

class Profile : Fragment() {

    companion object {
        fun newInstance() = Profile()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding : ProfileFragmentBinding
    private val productAdapter by lazy { ItemProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        setAdapters()
        setObservables()

    }

    private fun setObservables() {
        viewModel.listProduct.observe(viewLifecycleOwner, Observer {  onProductListRetrieved(it)})

        binding.ivOpenProfileDetail.setOnClickListener {
            val intent = Intent(activity, ProfileDetail::class.java)
            startActivity(intent)
        }

    }

    private fun onProductListRetrieved(productList: List<Product>) {
        if(productList.isEmpty()){
            // no itens
        }else{
            productAdapter.data = productList
        }

    }
    private fun setAdapters() {
        binding.rvMyitens.adapter = productAdapter
    }




}
