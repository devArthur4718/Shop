package com.stetter.escambo.ui.core.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ProfileFragmentBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.RecentPost
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.ui.adapter.ItemProductAdapter
import com.stetter.escambo.ui.adapter.RecentProductAdapter
import com.stetter.escambo.ui.base.BaseFragment

class Profile : BaseFragment() {

    companion object {
        fun newInstance() = Profile()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding : ProfileFragmentBinding
    private val recentProduct by lazy { RecentProductAdapter() }


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
        setObservables()
        setAdapters()

    }

    private fun setAdapters() {
        binding.rvRecentPosts.adapter = recentProduct
    }

    private fun setObservables() {
        //Retrieve user
        mainViewModel.getUserDataFromDatabase()
        mainViewModel.userProfileData.observe(viewLifecycleOwner, Observer { onUserDataReceveid(it) })
        viewModel.listRecentPost.observe(viewLifecycleOwner, Observer {onRecentPostListRetrieved(it) })
        binding.ivOpenProfileDetail.setOnClickListener {
            val intent = Intent(activity, ProfileDetail::class.java)
            startActivity(intent)
        }

        viewModel.retriveUserPostedProducts()

    }

    private fun onRecentPostListRetrieved(recentPostList: List<RecentPost>) {
        if(recentPostList.isEmpty()){
            //no itens
        }else{
            recentProduct.data = recentPostList
        }

    }

    private fun onUserDataReceveid(userData: RegisterUser?) {
        userData?.let {
            //Update UI
            binding.tvLoggedUserProfile.text =  it.fullName
            binding.tvLoggedUserLocation.text = it.city + "/" + it.uf
            //Load User profile
            val storage = FirebaseStorage.getInstance()
            if(it.photoUrl.length > 1){
                val gsReference = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com${it.photoUrl}")
                GlideApp.with(this)
                    .load(gsReference)
                    .placeholder(context?.CircularProgress())
                    .into(binding.ivProfileImage)

            }else{
                binding.ivProfileImage.setImageDrawable(resources.getDrawable(R.drawable.ic_young))
            }

        }
    }
}
