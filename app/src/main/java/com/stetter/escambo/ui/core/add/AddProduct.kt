package com.stetter.escambo.ui.core.add

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.stetter.escambo.R
import com.stetter.escambo.databinding.AddProductFragmenetBinding
import com.stetter.escambo.extension.checkCameraPermissions
import com.stetter.escambo.extension.showPickImageDialog
import com.stetter.escambo.net.models.SendProduct
import com.stetter.escambo.ui.adapter.ProductCard
import com.stetter.escambo.ui.adapter.UploadItemAdapter
import com.stetter.escambo.ui.base.BaseFragment
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class AddProduct : BaseFragment() {

    companion object {
        fun newInstance() = AddProduct()
        const val RQ_PICK_PHOTO = 0
        const val RQ_TAKE_PHOTO = 1
    }

    private lateinit var viewModel: AddProductViewModel
    private lateinit var binding : AddProductFragmenetBinding
    private lateinit var adapterSpinner  : ArrayAdapter<String>
    private val uploadItemAdapter by lazy {UploadItemAdapter()}

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
        setAdapters()
        setObserbales()
    }

    private fun setAdapters() {

        binding.rvUploadItem.adapter = uploadItemAdapter
    }

    private fun setObserbales() {
        viewModel.listCategorList.observe(viewLifecycleOwner, Observer { onConfigureCategoryAdapter(it) })
        viewModel.pickPhotoFromGallery.observe(viewLifecycleOwner, Observer { onPickDataFromGallery(it) })
        viewModel.imagePickIntent.observe(viewLifecycleOwner, Observer { onPickImageIntent(it) })
        viewModel.cameraPickintent.observe(viewLifecycleOwner, Observer { onCameraIntent(it) })
        viewModel.uploadSucess.observe(viewLifecycleOwner, Observer { onImageUploadSucess(it) })
        viewModel.productPath.observe(viewLifecycleOwner, Observer { onProductPathChange(it) })
        viewModel.uploadProduct.observe(viewLifecycleOwner, Observer { onProductUpload(it) })
        viewModel.loadingProgress.observe(viewLifecycleOwner, Observer { onLoading(it) })
        viewModel.listProduct.observe(viewLifecycleOwner, Observer { onProductListReceveived(it) })

        binding.btnPublishItem.setOnClickListener {
            val uid = viewModel.getUid()
            var category = binding.spCategory.selectedItem.toString()

            val product = SendProduct(uid,
                                   viewModel.getPaths(),
                                    binding.edtItemName.text.toString(),
                                    binding.edtItemDescription.text.toString(),
                            category, binding.edtItemValue.text.toString().toDouble())
            viewModel.uploadProductToFirebase(uid,product)
        }

        binding.labelPublishItem.setOnClickListener {
            viewModel.addItemToUpload()
        }
    }

    private fun onProductListReceveived(listProduct: List<ProductCard>) {
        if(listProduct.isEmpty()){

        }else{
            uploadItemAdapter.viewModel = viewModel
            uploadItemAdapter.data = listProduct
        }
    }

    private fun onLoading(it: Boolean?) {
        it?.let {
            if(it){
                mainViewModel.showLoading()
            }else{
                mainViewModel.hideLoading()
            }
        }
    }

    private fun onCameraIntent(it: Boolean?) {
        it?.let {
            if(it){
                pickFromCamera()
            }
        }
    }

    private fun pickFromCamera() {
        if(activity?.checkCameraPermissions() ?: false){
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                activity?.packageManager?.let {
                    takePictureIntent.resolveActivity(it)?.also {
                        startActivityForResult(takePictureIntent, RQ_TAKE_PHOTO)
                    }
                }
            }
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, RQ_PICK_PHOTO)
    }

    private fun onPickImageIntent(it: Boolean?) {
       it?.let {
           if(it){
               pickImage()
           }
       }
    }

    private fun onProductUpload(it: Boolean?) {
        it?.let {
            if(it){
                viewModel.doneUploadProduct()
                Toast.makeText(context, "Produto postado", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.navigation_explore)
            }
        }
    }

    var productPath = ""
    private fun onProductPathChange(path: String?) {
        path?.let {
            productPath = it
            viewModel.addPaths(productPath)
        }
    }

    private fun onImageUploadSucess(status: Boolean?) {
        status?.let {
            if(it){
                Toast.makeText(context, "Foto carregada com sucesso!", Toast.LENGTH_SHORT).show()
                cardbitmap?.let { bitmap -> viewModel.updateItemCard(bitmap) }
            }
        }
    }

    private fun onPickDataFromGallery(pickAction: Boolean?) {
        pickAction?.let {
            if(it){
                activity?.showPickImageDialog(viewModel)
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
                        viewModel.onPickPhotoSuccess()
                        viewModel.closePhotoIntent()
                        selectedPhotoUri = data.data
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedPhotoUri)
                        val bitmapDrawable = BitmapDrawable(bitmap)
                        encodeImageAndSaveToFirebase(bitmap)
                    }
                }
            }

            RQ_TAKE_PHOTO -> {
                if(resultCode == Activity.RESULT_OK) {
                    data?.let {
                        viewModel.closeCameraIntent()
                        selectedPhotoUri = data.data
                        val imageBitmap = it?.extras?.get("data") as Bitmap
                        encodeImageAndSaveToFirebase(imageBitmap)
                    }

                }
            }
        }
    }

    var cardbitmap : Bitmap? = null
    private fun encodeImageAndSaveToFirebase(bitmap : Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos)
        val imageEncoded = android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT)
        val filename = UUID.randomUUID().toString()
        cardbitmap = bitmap
        viewModel.uploadImageToFirebase(filename, imageEncoded)

    }

}
