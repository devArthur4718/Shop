package com.stetter.escambo.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.stetter.escambo.net.firebase.auth.LoginRepository
import com.stetter.escambo.net.firebase.database.FirestoreRepository
import com.stetter.escambo.net.firebase.storage.DatabaseRepository
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.net.retrofit.postalApi
import com.stetter.escambo.net.retrofit.responses.postalResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    var authRepository = LoginRepository()
    var database = DatabaseRepository()
    val db = FirestoreRepository()

    private val _addressValue = MutableLiveData<postalResponse>()
    val addressValue: LiveData<postalResponse> get() = _addressValue

    private val _loadingProgress = MutableLiveData<Boolean>()
    val loadingProgress: LiveData<Boolean> get() = _loadingProgress

    private val _showErrorDialog = MutableLiveData<Boolean>()
    val showErrorDialog: LiveData<Boolean> get() = _showErrorDialog

    private val _showRegisterError = MutableLiveData<String>()
    val showRegisterError: LiveData<String> get() = _showRegisterError

    private val _registerObserver = MutableLiveData<Boolean>()
    val registerObserver : LiveData<Boolean> get() = _registerObserver

    private val _loadingDialog = MutableLiveData<Boolean>()
    val loadingDialog : LiveData<Boolean> get() = _loadingDialog


    fun getAddress(cep: String) {
        _loadingDialog.value = true
        postalApi.retrofitService.getPostalCodes(cep).enqueue(object : Callback<postalResponse> {
            override fun onFailure(call: Call<postalResponse>, t: Throwable) {
                _loadingProgress.value = false
                _showErrorDialog.value = true
            }
            override fun onResponse(
                call: Call<postalResponse>,
                response: Response<postalResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.erro?.let {
                        if (it) {
                            _loadingDialog.value = false
                            _showErrorDialog.value = true

                        } else {
                            _addressValue.value = response.body()
                            _loadingDialog.value = false
                        }
                    }
                }

            }
        })
    }

    fun registerUser(sendUser: RegisterUser, password : String)  {
        _loadingProgress.value = true
        authRepository.createUser(sendUser.email, password)
            .addOnCompleteListener {task ->
                _loadingProgress.value = false
                if(task.isSuccessful){
                    saveUserData(sendUser, task.result?.user?.uid)
                }else{
                    try {
                        throw task.exception!!
                    }catch (e : FirebaseAuthUserCollisionException){
                        Log.e( "Auth", task.exception.toString())
                        _showRegisterError.value = "Este e-mail já está em uso!"
                    }
                }
            }.addOnFailureListener {
                Log.e( "Auth", it.toString())
                _registerObserver.value = false
                _loadingProgress.value = false
            }
    }


    fun showLoading(){
        _loadingProgress.value = true
    }

    fun hideLoading(){
        _loadingProgress.value = false
    }

    //TODO : Salvar usuários e seus dados - Done
    //Todo : Ler dados de um usuário - Done
    //Todo : Alterar dados de um usuário

    //Todo : Tabela produtos.
    //Todo : Salvar um produto com a chave do usuario(chave estrangeira.
    //Todo :  Ler e filtrar os mais próximos (calcule a distancia aqui no app).
    //Todo : Ler e filtrar os mais recentes
    //Todo : Ler e filtra os que tem mais matches e produtos. (média ponderada)
    //Todo : Ler todos os produtos de um usuário e exibir na tela
    //Todo : Editar ou excluir o produto de um usuário.


    fun saveUserData(user : RegisterUser, uid : String?){
        uid?.let { value -> user.clientID = value  }
        _loadingProgress.value = true

        db.insertUser()
            .document(uid!!)
            .set(user)
            .addOnSuccessListener {
                hideLoading()
                _loadingProgress.value = false
                _registerObserver.value = true
                    Log.d("Save User", "Sucesss")
            }
            .addOnFailureListener {
                hideLoading()
                _loadingProgress.value = false
                _registerObserver.value = false
                Log.e("Register", "error when creanting user $uid : $it")
            }

    }

}