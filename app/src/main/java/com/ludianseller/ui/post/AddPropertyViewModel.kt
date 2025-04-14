package com.ludianseller.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludianseller.repository.SellerDataRepository
import com.ludianseller.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(private  val  sellerDataRepository: SellerDataRepository) : ViewModel() {

    val addPropertyResponseLiveData: LiveData<NetworkResult<ResponseBody>?>

    get() = sellerDataRepository.sellerResponseLiveData




    fun addProperty(
        token: RequestBody,
        userId: RequestBody,
        title: RequestBody,
        address: RequestBody,
        lat: RequestBody,
        long: RequestBody,
        description: RequestBody,
        bedrooms: RequestBody,
        guests: RequestBody,
        house: RequestBody,
        bathrooms: RequestBody,
        size: RequestBody,
        price: RequestBody,
        wifi: RequestBody,
        breakfast: RequestBody,
        pets: RequestBody,
        squreMeter: RequestBody,
        image: List<MultipartBody.Part>

      //  image: Array<MultipartBody.Part>

    ){
        viewModelScope.launch {

            sellerDataRepository.addPropertyRepo(token,userId, title, address, lat, long, description, bedrooms, guests, house, bathrooms, size, price, wifi, breakfast,pets,squreMeter, image)
        }
    }


    fun addMyProperty(
        token: RequestBody,
        userId: RequestBody,
        propertyCategoryId: RequestBody,
        rentId: RequestBody,
        rentStringName: RequestBody,
        lat: RequestBody,
        lon: RequestBody,
        address: RequestBody,
        bathroomCount: RequestBody,
        bedCount: RequestBody,
        bedroomCount: RequestBody,
        guestQuantity: RequestBody,
        selectFacilityId: RequestBody,
        selectAmenitiesId: RequestBody,
        selectSafetyId: RequestBody,
        title: RequestBody,
        description: RequestBody,
        requestAcceptTypeId: RequestBody,
        price: RequestBody,
        discountId:RequestBody,
        pets:RequestBody,
        squreMeter:RequestBody,
        image: List<MultipartBody.Part>
    ){
        viewModelScope.launch {

            sellerDataRepository.addMyPropertyRepo(token,userId,propertyCategoryId,rentId,rentStringName,lat,
                lon,address,bathroomCount,bedCount,bedroomCount,guestQuantity,selectFacilityId,selectAmenitiesId,
                selectSafetyId,title,description,requestAcceptTypeId,price,discountId,pets,squreMeter,image)
        }
    }


    fun clearData(){
        sellerDataRepository.clearData()
    }


}