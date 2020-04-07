package com.stetter.escambo.ui.core.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.stetter.escambo.R
import com.stetter.escambo.databinding.ProfileFragmentBinding
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.ui.adapter.ItemProductAdapter
import com.stetter.escambo.ui.base.BaseFragment

class Profile : BaseFragment() {

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
        //Retrieve user
        mainViewModel.getUserDataFromDatabase()
        viewModel.listProduct.observe(viewLifecycleOwner, Observer {  onProductListRetrieved(it)})
        mainViewModel.userProfileData.observe(viewLifecycleOwner, Observer { onUserDataReceveid(it) })

        binding.ivOpenProfileDetail.setOnClickListener {
            val intent = Intent(activity, ProfileDetail::class.java)
            startActivity(intent)
        }

    }

    private fun onUserDataReceveid(userData: RegisterUser?) {
        userData?.let {
            //Update UI
            binding.tvLoggedUserProfile.text =  it.fullName
            binding.tvLoggedUserLocation.text = it.city + "/" + it.uf
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
