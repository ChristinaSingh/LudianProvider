package com.ludianseller.ui.post

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
class ExitingPropertyViewModel @Inject constructor(private val sellerDataRepository: SellerDataRepository) :
    ViewModel() {

    val exitingPropertyDataLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.exitingPropertyDataLiveData



    fun getExitingProperty(token:String) {
        viewModelScope.launch {
            sellerDataRepository.getExitingPropertyRepo(token)
        }
    }


    fun clearData() {
        sellerDataRepository.clearExitingPropertyData()
    }

}