package com.stetter.escambo.ui.core.add

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.Product
import com.stetter.escambo.ui.adapter.ProductCard
import java.util.*
import kotlin.collections.ArrayList

class AddProductViewModel : ViewModel() {

    val databaserepository = DatabaseRepository()

    private val _listCategoryProduct = MutableLiveData<List<String>>()
    val listCategoryList: LiveData<List<String>> get() = _listCategoryProduct

    private val _pickFromGalleryObservable = MutableLiveData<Boolean>()
    val pickPhotoFromGallery: LiveData<Boolean> get() = _pickFromGalleryObservable

    private val _imagePickIntent = MutableLiveData<Boolean>()
    val imagePickIntent: LiveData<Boolean> get() = _imagePickIntent

    private val _cameraPickintent = MutableLiveData<Boolean>()
    val cameraPickintent: LiveData<Boolean> get() = _cameraPickintent

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSucess: LiveData<Boolean> get() = _uploadSuccess

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean> get() = _loadingProgress

    private val _productPath = MutableLiveData<String>()
    val productPath: LiveData<String> get() = _productPath

    private val _uploadProduct = MutableLiveData<Boolean>()
    val uploadProduct: LiveData<Boolean> get() = _uploadProduct

    private val _listProduct = MutableLiveData<ArrayList<ProductCard>>()
    val listProduct: LiveData<ArrayList<ProductCard>> get() = _listProduct
    var adapterDummyList = ArrayList<ProductCard>()

    private val _pathLists = MutableLiveData<ArrayList<String>>()
    var pathLists = ArrayList<String>()

    private val _querryCategories = MutableLiveData<ArrayList<String>>()
    val querryCategories: LiveData<ArrayList<String>> get() = _querryCategories
    var categoriesList = ArrayList<String>()

    init {

        var dummyList = listOf("Categoria", "Produto 1", "Produto 2", "Produto 3", "Produto 4")
        adapterDummyList.add(ProductCard(null))

        _listProduct.value = adapterDummyList
        _listCategoryProduct.value = dummyList
    }


    private val _loadingPhotoProgress = MutableLiveData<Boolean>()
    val loadingPhotoProgress: LiveData<Boolean> get() = _loadingPhotoProgress

    fun uploadImageToFirebase(filename: String, byteArray: ByteArray) {
        _loadingPhotoProgress.value = true
        databaserepository.uploadImageToDatabase(filename).putBytes(byteArray)
            .addOnSuccessListener {
                _productPath.value = it.metadata?.path
                _loadingPhotoProgress.value = false
                _uploadSuccess.value = true
            }
            .addOnFailureListener {
                _uploadSuccess.value = false
                _loadingPhotoProgress.value = false
            }
    }
    fun updateProductCount(count : Int){
        _loadingProgress.value = true
        databaserepository.updateProductCount(count)
            .addOnCompleteListener {
                _loadingProgress.value = false

            }.addOnFailureListener {

                _loadingProgress.value = false
            }
    }

    fun uploadImageToFirebase(filename: String, uri: Uri) {
        _loadingProgress.value = true
        databaserepository.uploadImageToDatabase(filename).putFile(uri)
            .addOnSuccessListener {
                _productPath.value = it.metadata?.path
                _loadingProgress.value = false
                _uploadSuccess.value = true

            }
            .addOnFailureListener {
                _uploadSuccess.value = false
                _loadingProgress.value = false
            }
    }

    fun uploadProductToFirebase(
        product: Product,
        productList: ArrayList<String>
    ) {
        _loadingProgress.value = true
        var productUID = UUID.randomUUID().toString()
        databaserepository.updateProductToDabatase(productUID).ref.setValue(product)
            .addOnSuccessListener {
                productList.add(productUID)
                updateUserListedProducts(productList)
                _uploadProduct.value = true
                _loadingProgress.value = false
            }.addOnFailureListener {
                _uploadProduct.value = false
                _loadingProgress.value = false
            }

    }

    private fun updateUserListedProducts(productUID : ArrayList<String>) {
        databaserepository.updateUserProductList(productUID)
            .addOnCompleteListener {
                Log.d("AddProduct", "Success")
            }.addOnFailureListener {
                Log.d("AddProduct", "Error $it")
            }
    }

    fun fetchProductCategories(){
        var categoriesList = ArrayList<String>()
        databaserepository.receiveCategories().addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) { }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) { }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) { }
            override fun onChildAdded(data: DataSnapshot, p1: String?) {
                var text = data.getValue(String::class.java)
                text?.let { categoriesList.add(it) }
                _listCategoryProduct.value  = categoriesList
            }
            override fun onChildRemoved(p0: DataSnapshot) { }
        })
    }

    fun getUid(): String {
        return databaserepository.currentUserUID()
    }

    fun pickPhoto() {
        _pickFromGalleryObservable.value = true
    }

    fun onPickPhotoSuccess() {
        _pickFromGalleryObservable.value = false
    }

    fun openPickPhotoIntent() {
        _imagePickIntent.value = true
    }

    fun closePhotoIntent() {
        _imagePickIntent.value = false
    }

    fun openCameraIntent() {
        _cameraPickintent.value = true
    }

    fun closeCameraIntent() {
        _cameraPickintent.value = false
    }

    fun doneUploadProduct() {
        _uploadProduct.value = false
    }

    fun addItemToUpload() {
        adapterDummyList.add(ProductCard(null))
        _listProduct.value = adapterDummyList
    }

    fun updateItemCard(bitmap: Bitmap) {
        if (adapterDummyList.size == 5) return
        adapterDummyList.add(ProductCard(bitmap))
        _listProduct.value = adapterDummyList
    }

    fun addPaths(path: String) {
        pathLists.add(path)
    }

    fun getPaths(): ArrayList<String> {
        return pathLists
    }

}
