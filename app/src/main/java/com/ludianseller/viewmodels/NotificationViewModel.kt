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
class NotificationViewModel @Inject constructor(private val sellerDataRepository: SellerDataRepository) : ViewModel() {

    val notificationLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.notificationLiveData


    fun notificationData(map:Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.notificationRepo(map)
        }
    }

    fun clearNotificationData() {
        sellerDataRepository.clearNotificationData()
    }

}