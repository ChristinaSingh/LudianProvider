package com.ludianseller.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludianseller.repository.SellerDataRepository
import com.ludianseller.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val sellerDataRepository: SellerDataRepository) : ViewModel() {

    val changePasswordLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.changePasswordLiveData

    val faqLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.faqLiveData

    val aboutUsLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.aboutUsLiveData

    val privacyPolicyLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.privacyPolicyLiveData




    fun changePassword(map: Map<String, String>){
        viewModelScope.launch {
            sellerDataRepository.changePasswordRepo(map)
        }
    }


    fun getFaq(token: String){
        viewModelScope.launch {
            sellerDataRepository.faqRepo(token)
        }
    }

    fun getAboutUs(token: String){
        viewModelScope.launch {
            sellerDataRepository.aboutUsRepo(token)
        }
    }

    fun getPrivacyPolicy(token:String){
        viewModelScope.launch {
            sellerDataRepository.privacyPolicyRepo(token)
        }
    }




    fun clearChangePasswordData() {
        sellerDataRepository.clearChangePasswordData()
    }


    fun clearFaqData() {
        sellerDataRepository.clearFaqData()
    }

    fun clearAboutUsData() {
        sellerDataRepository.clearAboutUsData()
    }

    fun clearPrivacyPolicyData() {
        sellerDataRepository.clearPrivacyPolicyData()
    }

}