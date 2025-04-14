package com.ludianseller.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ludianseller.api.SellerAPI
import com.ludianseller.utils.NetworkResult
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class SellerRepository @Inject constructor(private val sellerAPI: SellerAPI) {

    private val _sellerResponseLiveData = MutableLiveData<NetworkResult<ResponseBody>>()
    val sellerResponseLiveData: LiveData<NetworkResult<ResponseBody>>
        get() = _sellerResponseLiveData

    suspend fun registerUser(params: Map<String, String>) {
        _sellerResponseLiveData.postValue(NetworkResult.Loading())
        val response = sellerAPI.signup(params)
        handleResponse(response)
    }

    suspend fun loginUser(params: Map<String, String>) {
        _sellerResponseLiveData.postValue(NetworkResult.Loading())
        val response =sellerAPI.login(params)
        handleResponse(response)
    }


    suspend fun forgotPasswordRepo(params: Map<String, String>) {
        _sellerResponseLiveData.postValue(NetworkResult.Loading())
        val response =sellerAPI.forgotPasswordApi(params)
        handleResponse(response)
    }


    suspend fun otpVerifyRepo(params: Map<String, String>) {
        _sellerResponseLiveData.postValue(NetworkResult.Loading())
        val response =sellerAPI.otpVerifyApi(params)
        handleResponse(response)
    }


    suspend fun createNewPasswordRepo(params: Map<String, String>) {
        _sellerResponseLiveData.postValue(NetworkResult.Loading())
        val response =sellerAPI.createNewPasswordApi(params)
        handleResponse(response)
    }






    private fun handleResponse(response: Response<ResponseBody>) {
        if (response.isSuccessful && response.body() != null) {
            _sellerResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _sellerResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            _sellerResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}