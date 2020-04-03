package com.stetter.escambo.ui.core.add

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage

import com.stetter.escambo.R
import com.stetter.escambo.databinding.AddProductFragmenetBinding
import java.util.*

class AddProduct : Fragment() {

    companion object {
        fun newInstance() = AddProduct()
        const val RQ_PICK_PHOTO = 0
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
        binding.viewmodel = viewModel
        setObserbales()
    }

    private fun setObserbales() {
        viewModel.listCategorList.observe(viewLifecycleOwner, Observer { onConfigureCategoryAdapter(it) })
        viewModel.pickPhotoFromGallery.observe(viewLifecycleOwner, Observer { onPickDataFromGallery(it) })
    }

    private fun onPickDataFromGallery(pickAction: Boolean?) {

        pickAction?.let {
            if(it){
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, RQ_PICK_PHOTO)
            }
        }
    }

    private fun onConfigureCategoryAdapter(categoryList: List<String>) {
        adapterSpinner = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, categoryList)
        binding.spCategory.adapter = adapterSpinner
    }

    var selectedPhotoUri : Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when(requestCode){
            RQ_PICK_PHOTO -> {
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        selectedPhotoUri = data.data
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedPhotoUri)
                        val bitmapDrawable = BitmapDrawable(bitmap)
                        binding.groupPickPhoto.visibility = View.INVISIBLE
                        binding.lvLoadedProduct.setImageDrawable(bitmapDrawable)
                        sendImageToFirebase()
                    }
                }
            }
        }
    }

    private fun sendImageToFirebase() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        selectedPhotoUri?.let {
            ref.putFile(it)
                .addOnSuccessListener {
                        Toast.makeText(context, "Upload com sucesso: ${it.metadata?.path} ", Toast.LENGTH_SHORT).show()
                }

        }
    }
}
