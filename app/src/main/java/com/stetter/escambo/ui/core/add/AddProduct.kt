package com.stetter.escambo.ui.core.add

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.stetter.escambo.R
import com.stetter.escambo.databinding.AddProductFragmenetBinding
import com.stetter.escambo.extension.*
import com.stetter.escambo.extension.dialogs.checkCameraPermissions
import com.stetter.escambo.extension.dialogs.showPickImageDialog
import com.stetter.escambo.extension.watcher.MoneyTextWatcher
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.ui.adapter.ProductCard
import com.stetter.escambo.ui.adapter.UploadItemAdapter
import com.stetter.escambo.ui.base.BaseFragment
import com.stetter.escambo.ui.core.CoreActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddProduct : BaseFragment() {

    companion object {
        fun newInstance() = AddProduct()
        const val RQ_PICK_FROM_GALLERY = 0
        const val RQ_TAKE_PHOTO = 1
        const val QUALITY_COMPRESS = 50
        const val MAX_IMAGE_SIZE = 600f
    }

    private lateinit var viewModel: AddProductViewModel
    private lateinit var binding: AddProductFragmenetBinding
    private lateinit var adapterSpinner: ArrayAdapter<String>
    private val uploadItemAdapter by lazy { UploadItemAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_product_fragmenet,
            container,
            false
        )

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
//        mainViewModel.saveCurrentUID()
        viewModel.fetchProductCategories()
        viewModel.listCategoryList.observe( viewLifecycleOwner,  Observer { onConfigureCategoryAdapter(it) })
        viewModel.pickPhotoFromGallery.observe( viewLifecycleOwner,  Observer { onPickDataFromGallery(it) })
        viewModel.imagePickIntent.observe(viewLifecycleOwner, Observer { onPickImageIntent(it) })
        viewModel.cameraPickintent.observe(viewLifecycleOwner, Observer { onCameraIntent(it) })
        viewModel.uploadSucess.observe(viewLifecycleOwner, Observer { onImageUploadSucess(it) })
        viewModel.productPath.observe(viewLifecycleOwner, Observer { onProductPathChange(it) })
        viewModel.uploadProduct.observe(viewLifecycleOwner, Observer { onProductUpload(it) })
        viewModel.loadingProgress.observe(viewLifecycleOwner, Observer { onLoading(it) })
        viewModel.listProduct.observe(viewLifecycleOwner, Observer { onProductListReceveived(it) })
        viewModel.loadingPhotoProgress.observe(viewLifecycleOwner, Observer { onLoadingPhotoProgress(it) })
        viewModel.querryCategories.observe(viewLifecycleOwner, Observer { onCategoryListReceived(it) })
        mainViewModel.userProfileData.observe(viewLifecycleOwner, Observer { onUserDataReceveid(it) })

        binding.btnPublishItem.setOnClickListener {
            sendProduct()
        }

        binding.labelPublishItem.setOnClickListener {
            viewModel.addItemToUpload()
        }

        binding.edtItemValue.addTextChangedListener(MoneyTextWatcher(binding.edtItemValue, Locale("pt", "BR")))
    }

    private fun sendProduct() {

        binding.edtItemName.clearError()
        binding.edtItemValue.clearError()
        binding.edtItemDescription.clearError()

        when{
            binding.edtItemName.text.isNullOrEmpty() -> {
                binding.edtItemName.setError(getString(R.string.blank_name))
                return
            }
            binding.edtItemDescription.text.isNullOrEmpty() -> {
                binding.edtItemDescription.setError(getString(R.string.blank_text))
                return
            }
            binding.edtItemValue.text.isNullOrEmpty() -> {
                binding.edtItemValue.setError(getString(R.string.emmpty_field))
                return
            }
            viewModel.getPaths().isEmpty() -> {
                showDialog(getString(R.string.load_product_photo_error)).show()
                return
            }
        }

        val product = Product(
            viewModel.getUid(),
            viewModel.getPaths(),
            binding.edtItemName.text.toString(),
            binding.edtItemDescription.text.toString(),
            binding.spCategory.selectedItem.toString(),
            binding.edtItemValue.text.toString(),
            Calendar.getInstance().getTimeStamp(),
            fullName,
            userPhotoUrl,
            lat,
            lng,
            uf,
            city
        )
        viewModel.uploadProduct(product)
        //upload user product count
    }

    private fun onCategoryListReceived(categoryList: List<String>) {

    }

    private fun showDialog(text: String): AlertDialog {
        val alertDialog: AlertDialog? = this?.let {
            val builder = AlertDialog.Builder(context)
            builder.apply {
                setPositiveButton(
                    getString(R.string.confirm_ok),
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })

                setMessage(text)
            }
            builder.create()
        }
        return alertDialog!!
    }

    private fun onLoadingPhotoProgress(it: Boolean?) {
        it?.let {
            if(it){
                binding.progressBar3.visibility = View.VISIBLE
            }else{
                binding.progressBar3.visibility = View.GONE
            }
        }

    }

    var fullName = ""
    var userPhotoUrl = ""
    var lat = 0.0
    var lng = 0.0
    var uf = ""
    var city = ""
    var productCount = 0


    private fun onUserDataReceveid(it: RegisterUser?) {
        it?.let {
            fullName = it.fullName
            userPhotoUrl = it.photoUrl
            lat = it.lat
            lng = it.lng
            uf = it.uf
            city = it.city
            productCount = it.products
        }
    }

    private fun onProductListReceveived(listProduct: List<ProductCard>) {
        if (listProduct.isEmpty()) {

        } else {
            uploadItemAdapter.viewModel = viewModel
            uploadItemAdapter.data = listProduct
        }
    }

    private fun onLoading(it: Boolean?) {
        it?.let {
            if (it) {
                mainViewModel.showLoading()
            } else {
                mainViewModel.hideLoading()
            }
        }
    }

    private fun onCameraIntent(it: Boolean?) {
        it?.let {
            if (it) {
                openCameraIntent()
            }
        }
    }


    var photoFile : File? = null
    private fun openCameraIntent() {
        if (activity?.checkCameraPermissions() == true) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                activity?.packageManager?.let {
                    takePictureIntent.resolveActivity(it)?.also {
                        // Create the File where the photo should go
                        photoFile = try {
                            createImageFile()
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            Log.e("AddProduct", "Error $ex")
                            null
                        }
                        photoFile?.also { file ->
                            val photoURI : Uri = FileProvider.getUriForFile(
                                context!!,
                                "com.stetter.escambo.fileprovider",
                                file
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

                            startActivityForResult(takePictureIntent, RQ_TAKE_PHOTO)
                        }
                    }
                }
            }
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, RQ_PICK_FROM_GALLERY)
    }

    private fun onPickImageIntent(it: Boolean?) {
        it?.let {
            if (it) {
                pickImage()
            }
        }
    }

    private fun onProductUpload(it: Boolean?) {
        it?.let {
            if (it) {
                viewModel.doneUploadProduct()
                productCount += 1
                viewModel.updateProductCount(productCount)
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
            if (it) {
                Toast.makeText(context, "Foto carregada com sucesso!", Toast.LENGTH_SHORT).show()
                cardbitmap?.let { bitmap -> viewModel.updateItemCard(bitmap) }
            }
        }
    }

    private fun onPickDataFromGallery(pickAction: Boolean?) {
        pickAction?.let {
            if (it) {
                activity?.showPickImageDialog(viewModel)
            }
        }
    }

    private fun onConfigureCategoryAdapter(categoryList: List<String>) {
        adapterSpinner = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, categoryList)
        binding.spCategory.adapter = adapterSpinner
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RQ_PICK_FROM_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        viewModel.onPickPhotoSuccess()
                        viewModel.closePhotoIntent()
                        selectedPhotoUri = data.data
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            selectedPhotoUri
                        )
                        selectedPhotoUri?.let { uri -> compressImage(bitmap, uri, RQ_PICK_FROM_GALLERY) }
                    }
                }
            }
            RQ_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.closeCameraIntent()
                    selectedPhotoUri = Uri.fromFile(photoFile)

                    val bitmap = MediaStore.Images.Media.getBitmap(
                        activity?.contentResolver,
                        selectedPhotoUri
                    )


                    selectedPhotoUri?.let { uri -> compressImage(bitmap, uri, RQ_TAKE_PHOTO) }
                }
            }
        }
    }

    var cardbitmap: Bitmap? = null
    private fun compressImage(bitmap: Bitmap, uri: Uri, type : Int) {
        when(type){
            RQ_PICK_FROM_GALLERY -> {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 25, baos)
                val filename = UUID.randomUUID().toString()
                val data = baos.toByteArray()
                cardbitmap = bitmap
                viewModel.uploadImageToFirebase(filename, data)

            }
            RQ_TAKE_PHOTO -> {

                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_COMPRESS, baos)
                val data = baos.toByteArray()

                val filename = UUID.randomUUID().toString()
                cardbitmap = scaleDown(bitmap,MAX_IMAGE_SIZE, true)
                viewModel.uploadImageToFirebase(filename, data)

            }
        }

    }

    private fun scaleDown(inputImage : Bitmap, maxImageSize : Float, filter : Boolean) : Bitmap{
        val ratio = Math.min(maxImageSize/ inputImage.width, maxImageSize / inputImage.height)
        val width = Math.round(ratio * inputImage.width)
        val height = Math.round(ratio * inputImage.height)
        return Bitmap.createScaledBitmap(inputImage, width, height, filter)
    }


    var imageFilePath: String = ""
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "PNG_${timeStamp}_", /* prefix */
            ".png", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            imageFilePath = absolutePath
        }
    }

}
