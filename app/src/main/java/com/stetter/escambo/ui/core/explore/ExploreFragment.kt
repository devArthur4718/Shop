package com.stetter.escambo.ui.core.explore

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ExploreFragmentBinding
import com.stetter.escambo.extension.metersToKM
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.ProductByLocation
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.ui.adapter.ItemProductNextToMeAdapter
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
    private val productNextToMeAdapter by lazy { ItemProductNextToMeAdapter() }
    private val recentProduct by lazy { RecentProductAdapter() }
    private val topuserAdapter by lazy { TopUserAdapter() }
    var currentLat: Double? = null
    var currentLng: Double? = null

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
        binding.rvNextProducts.adapter = productNextToMeAdapter
        binding.rvTopUsers.adapter = topuserAdapter
        binding.rvRecentPosts.adapter = recentProduct
    }

    private fun setObservables() {
        viewModel.listNextProducts.observe(viewLifecycleOwner,  Observer { onProductListRetrieved(it) })
        viewModel.topUsersList.observe(viewLifecycleOwner, Observer { onTopUserListRetrieved(it) })
        viewModel.listRecentPost.observe( viewLifecycleOwner,  Observer { onRecentPostListRetrieved(it) })
        mainViewModel.onLocationReceived.observe(viewLifecycleOwner, Observer { onLocationReceived(it) })

        //Retrieve data from firebase
        mainViewModel.retrieveUserLocation()
        viewModel.selectProducts()
        viewModel.retrieveMostRatedUsers()

        binding.btnFilter.setOnClickListener {
            val intent = Intent(context, FilterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onLocationReceived(location: Location) {
        location?.let {
            currentLat = location.latitude
            currentLng = location.longitude
            viewModel.retrieveProductsNextToMe()
        }
    }

    private fun onRecentPostListRetrieved(recentPostList: List<Product>) {
        if (recentPostList.isEmpty()) {
            //no itens
        } else {
            recentProduct.data = recentPostList
        }
    }

    private fun onTopUserListRetrieved(topUserList: List<RegisterUser>) {
        if (topUserList.isEmpty()) {
            //no itens
        } else {

            topuserAdapter.data = topUserList
        }
    }

    //Products next to me
    private fun onProductListRetrieved(recentProductList: List<ProductByLocation>) {
        if (recentProductList.isEmpty()) {
            // no itens
        } else {
            //Show only products that are not mine.
            var filteredProducts = recentProductList.filter {it.uid != viewModel.retrieveUserUID()  }
            filteredProducts.forEach {
                it.distance = it.distanceBetween(currentLat, currentLng, it.lat, it.lng )
            }
            //Sort by the clossest
            productNextToMeAdapter.data = filteredProducts.sortedBy { it.distance }
        }
    }

}
