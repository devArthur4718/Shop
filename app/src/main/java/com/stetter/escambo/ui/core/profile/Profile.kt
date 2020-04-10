package com.stetter.escambo.ui.core.profile

import android.app.Activity
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
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.net.models.SendProduct
import com.stetter.escambo.ui.adapter.MyProductAdapter
import com.stetter.escambo.ui.base.BaseFragment
import com.stetter.escambo.ui.login.LoginActivity
import java.util.*
import kotlin.collections.ArrayList

class Profile : BaseFragment() {

    companion object {
        fun newInstance() = Profile()
        const val RC_FINISH_SESSION = 20
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding : ProfileFragmentBinding
    private val myProductAdapter by lazy { MyProductAdapter() }


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
        binding.rvRecentPosts.adapter = myProductAdapter
    }

    private fun setObservables() {
        //Retrieve user
        mainViewModel.getUserDataFromDatabase()
        mainViewModel.userProfileData.observe(viewLifecycleOwner, Observer { onUserDataReceveid(it) })
        viewModel.querryFirebase.observe(viewLifecycleOwner, Observer { onUserProductListReceived(it) })

        binding.ivOpenProfileDetail.setOnClickListener {
            val intent = Intent(activity, ProfileDetail::class.java)
            startActivityForResult(intent, RC_FINISH_SESSION)
        }

        viewModel.retriveUserPostedProducts()

    }

    private fun onUserProductListReceived(datalist: ArrayList<SendProduct>?) {
        datalist?.let {
            if(it.isEmpty()){
                binding.tvNoUserProducts.visibility = View.VISIBLE
            }else{
                myProductAdapter.data = datalist
                //Update product count label
                binding.tvProdutos.text = "${datalist.size} Produtos"
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RC_FINISH_SESSION -> {
                if(resultCode == Activity.RESULT_OK){
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }
}
