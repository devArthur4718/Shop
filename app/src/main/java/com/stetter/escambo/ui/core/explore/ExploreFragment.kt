package com.stetter.escambo.ui.core.explore

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
import com.stetter.escambo.databinding.ExploreFragmentBinding
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.ProductMock
import com.stetter.escambo.net.models.RecentPost
import com.stetter.escambo.net.models.TopUser
import com.stetter.escambo.ui.adapter.ItemProductAdapter
import com.stetter.escambo.ui.adapter.RecentProductAdapter
import com.stetter.escambo.ui.adapter.TopUserAdapter
import com.stetter.escambo.ui.core.explore.filter.FilterActivity

class ExploreFragment : Fragment() {

    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var viewModel: ExploreViewModel
    private lateinit var binding : ExploreFragmentBinding
    private val productAdapter by lazy {ItemProductAdapter()}
    private val recentProduct by lazy {RecentProductAdapter()}
    private val topuserAdapter by lazy {TopUserAdapter()}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.explore_fragment,
            container,
            false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ExploreViewModel::class.java)
        setAdapters()
        setObservables()
    }

    private fun setAdapters() {
        binding.rvPostedProducts.adapter = productAdapter
        binding.rvTopUsers.adapter = topuserAdapter
        //TODO: FETCH RECENT PRODUCTS AN ORDER BY TIME STAMP
        binding.rvRecentPosts.adapter = recentProduct
    }

    private fun setObservables() {
        viewModel.listProductMock.observe(viewLifecycleOwner, Observer {  onProductListRetrieved(it)})
        viewModel.listTopUser.observe(viewLifecycleOwner, Observer { onTopUserListRetrieved(it) })
        viewModel.listRecentPost.observe(viewLifecycleOwner, Observer {onRecentPostListRetrieved(it) })

        binding.btnFilter.setOnClickListener {
            val intent = Intent(context, FilterActivity::class.java)
            startActivity(intent)
        }


        //Retrieve recent products
        viewModel.retrieveRecentProducts()

    }

    private fun onRecentPostListRetrieved(recentPostList: List<Product>) {
        if(recentPostList.isEmpty()){
            //no itens

        }else{
            recentProduct.data = recentPostList
        }

    }

    private fun onTopUserListRetrieved(topUserList: List<TopUser>) {
        if(topUserList.isEmpty()){
            //no itens
        }else{
            topuserAdapter.data = topUserList
        }
    }

    private fun onProductListRetrieved(productMockList: List<ProductMock>) {
        if(productMockList.isEmpty()){
            // no itens
        }else{
            productAdapter.data = productMockList
        }
    }

}
