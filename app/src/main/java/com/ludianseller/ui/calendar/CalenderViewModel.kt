package com.ludianseller.ui.calendar

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
class CalenderViewModel @Inject constructor(private val sellerDataRepository: SellerDataRepository) :
    ViewModel() {

    val propertyAddedDataLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.propertyAddedDataLiveData






    fun getProperty(token:String) {
        viewModelScope.launch {
            sellerDataRepository.getAddedPropertyRepo(token)
        }
    }


    fun clearData() {
        sellerDataRepository.clearAddedProperData()
    }

}