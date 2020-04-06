package com.stetter.escambo.ui.core.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.SendProduct
import java.net.URI

class AddProductViewModel : ViewModel() {

    val databaserepository = DatabaseRepository()

    private val _listCategoryProduct = MutableLiveData<List<String>>()
    val listCategorList : LiveData<List<String>> get() = _listCategoryProduct

    private val _pickFromGalleryObservable = MutableLiveData<Boolean>()
    val pickPhotoFromGallery : LiveData<Boolean> get() = _pickFromGalleryObservable

    private val _imagePickIntent = MutableLiveData<Boolean>()
    val imagePickIntent : LiveData<Boolean> get() = _imagePickIntent

    private val _cameraPickintent = MutableLiveData<Boolean>()
    val cameraPickintent : LiveData<Boolean> get() = _cameraPickintent

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSucess : LiveData<Boolean> get() = _uploadSuccess

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress : LiveData<Boolean> get() = _loadingProgress

    private val _productPath = MutableLiveData<String>()
    val productPath : LiveData<String> get() = _productPath

    private val _uploadProduct = MutableLiveData<Boolean>()
    val uploadProduct : LiveData<Boolean> get() = _uploadProduct


    init {

        var dummyList = listOf<String>("Categoria", "Produto 1", "Produto 2", "Produto 3", "Produto 4")

        _listCategoryProduct.value = dummyList
    }

    fun uploadImageToFirebase(filename : String, uri : Uri) {
        _loadingProgress.value = true
        databaserepository.uploadImageToDatabase(filename).putFile(uri)
            .addOnSuccessListener {
                _productPath.value = it.metadata?.path
                _loadingProgress.value = false
                _uploadSuccess.value = true
            }
            .addOnFailureListener{
                _uploadSuccess.value = false
                _loadingProgress.value = false
            }

    }

    fun uploadProductToFirebase(uid : String , product : SendProduct){
        val ref = databaserepository.updateProductToDabatase(uid)
        ref.setValue(product)
            .addOnSuccessListener {
                _uploadProduct.value = true
            }
            .addOnFailureListener {
                _uploadProduct.value = false
            }
    }

    fun getUid() : String{
        return databaserepository.getUID()
    }

    fun pickPhoto(){
        _pickFromGalleryObservable.value = true
    }

    fun onPickPhotoSuccess(){
        _pickFromGalleryObservable.value = false
    }

    fun openPickPhotoIntent() {
       _imagePickIntent.value = true
    }
    fun closePhotoIntent(){
        _imagePickIntent.value = false
    }

    fun openCameraIntent() {
        _cameraPickintent.value = true
    }
    fun closeCameraIntent() {
        _cameraPickintent.value = false
    }

}
