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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ExploreFragmentBinding
import com.stetter.escambo.extension.metersToKM
import com.stetter.escambo.net.models.*
import com.stetter.escambo.ui.adapter.ItemProductAdapter
import com.stetter.escambo.ui.adapter.RecentProductAdapter
import com.stetter.escambo.ui.adapter.TopUserAdapter
import com.stetter.escambo.ui.base.BaseFragment
import com.stetter.escambo.ui.core.explore.filter.FilterActivity
import java.util.*

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

    }

    private fun setObservables() {
        viewModel.listNextProducts.observe(
            viewLifecycleOwner,
            Observer { onProductListRetrieved(it) })
        viewModel.topUsersList.observe(viewLifecycleOwner, Observer { onTopUserListRetrieved(it) })
        viewModel.listRecentPost.observe(
            viewLifecycleOwner,
            Observer { onRecentPostListRetrieved(it) })

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
            //Filter user with no matches
            var filteredList = topUserList.filter { it.matches > 0 }
            topuserAdapter.data = filteredList.reversed()
        }
    }

    lateinit var fusedLocationClient: FusedLocationProviderClient
    private fun onProductListRetrieved(recentProductList: List<ProductByLocation>) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        var currentLat: Double? = null
        var currentLng: Double? = null

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                currentLat = location?.latitude
                currentLng = location?.longitude
            }.addOnFailureListener { error ->
                Log.e("Explore", "Error: $error")
            }

        if (recentProductList.isEmpty()) {
            // no itens
        } else {

            //Show only products that are not mine.
            var filteredProducts = recentProductList.filter {it.uid != viewModel.retrieveUserUID()  }

            filteredProducts.forEach {
                it.distance = distanceBetween(currentLat, currentLng, it.lat, it.lng )
            }
            //Sort by the clossest
            productAdapter.data = filteredProducts.sortedBy { it.distance }
        }
    }

    private fun distanceBetween(lat1: Double?, lng1: Double?, lat2: Double?, lng2: Double?): Float {
        var locationA = Location("PointA")
        if (lat1 != null) {
            locationA.latitude = lat1
        }
        if (lng1 != null) {
            locationA.longitude = lng1
        }
        var locationB = Location("PointB")
        if (lat2 != null) {
            locationB.latitude = lat2
        }
        if (lng2 != null) {
            locationB.longitude = lng2
        }
        var distance = locationA.distanceTo(locationB)
        return distance.metersToKM().toFloat()
    }
}
