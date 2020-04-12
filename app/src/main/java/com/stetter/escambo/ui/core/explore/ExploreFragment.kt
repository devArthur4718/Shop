package com.stetter.escambo.ui.core.explore

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ExploreFragmentBinding
import com.stetter.escambo.extension.metersToKM
import com.stetter.escambo.net.models.*
import com.stetter.escambo.ui.adapter.ItemProductAdapter
import com.stetter.escambo.ui.adapter.RecentProductAdapter
import com.stetter.escambo.ui.adapter.TopUserAdapter
import com.stetter.escambo.ui.base.BaseFragment
import com.stetter.escambo.ui.core.explore.filter.FilterActivity

class ExploreFragment : BaseFragment() {

    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var viewModel: ExploreViewModel
    private lateinit var binding: ExploreFragmentBinding
    private val productAdapter by lazy { ItemProductAdapter() }
    private val recentProduct by lazy { RecentProductAdapter() }
    private val topuserAdapter by lazy { TopUserAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.explore_fragment,
            container,
            false
        )

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)
        setAdapters()
        setObservables()
    }

    private fun setAdapters() {
        binding.rvNextProducts.adapter = productAdapter
        binding.rvTopUsers.adapter = topuserAdapter
        binding.rvRecentPosts.adapter = recentProduct

//        var locationA = Location("Point A")
//        locationA.latitude = -46.633309399999995
//        locationA.longitude = -23.550519899999998

//        var locationB = Location("Point B")
//        locationB.latitude = -51.2176986
//        locationB.longitude = -30.0346316
//
//        var distance = locationA.distanceTo(locationB)
//        Log.d("Explore", "Distance: ${distance.metersToKM()} Meters")

    }

    private fun setObservables() {
        viewModel.listNextProducts.observe( viewLifecycleOwner, Observer { onProductListRetrieved(it) })
        viewModel.topUsersList.observe(viewLifecycleOwner, Observer { onTopUserListRetrieved(it) })
        viewModel.listRecentPost.observe(  viewLifecycleOwner,Observer { onRecentPostListRetrieved(it) })

        binding.btnFilter.setOnClickListener {
            val intent = Intent(context, FilterActivity::class.java)
            startActivity(intent)
        }

        //Retrieve recent products
        viewModel.retrieveRecentProducts()
        viewModel.retrieveTopUsers()
        viewModel.retrieveProductsCloseToMe()

    }

    private fun onRecentPostListRetrieved(recentPostList: List<Product>) {
        if (recentPostList.isEmpty()) {
            //no itens

        } else {
            recentProduct.data = recentPostList.reversed()
        }

    }

    private fun onTopUserListRetrieved(topUserList: List<RegisterUser>) {
        if (topUserList.isEmpty()) {
            //no itens
        } else {
            topuserAdapter.data = topUserList.reversed()
        }
    }

    private fun onProductListRetrieved(recentProductList: List<ProductByLocation>) {
        if (recentProductList.isEmpty()) {
            // no itens
        } else {

//            var locationA = Location("Point A")
//            locationA.latitude = 0.0
//            locationA.longitude = 0.0
//
//            var locationB = Location("Point B")
//            locationB.latitude = 0.0
//            locationB.longitude = 0.0

            productAdapter.data = recentProductList
        }
    }

}
