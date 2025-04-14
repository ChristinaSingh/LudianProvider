package com.ludianseller.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ludianseller.api.SellerDataApi
import com.ludianseller.utils.NetworkResult
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Part
import javax.inject.Inject


class SellerDataRepository @Inject constructor(private val sellerDataApi: SellerDataApi) {

    private val _sellerDataLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val sellerResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _sellerDataLiveData

    private val _propertyDataLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val propertyResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _propertyDataLiveData


    private val _amenitiesDataLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val amenitiesResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _amenitiesDataLiveData


    private val _facilitiesDataLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val facilitiesResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _facilitiesDataLiveData

    private val _safetyDataLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val safetyResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _safetyDataLiveData


    private val _propertyAddedDataLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val propertyAddedDataLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _propertyAddedDataLiveData


    private val _countryLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val countryLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _countryLiveData


    private val _documentLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val documentLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _documentLiveData


    private val _propertyPriceWithDayLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val propertyPriceWithDayLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _propertyPriceWithDayLiveData


    private val _updatePriceWithDayLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val updatePriceWithDayLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _updatePriceWithDayLiveData



    private val _bookingLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val bookingLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _bookingLiveData


    private val _searchBookingLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val searchBookingLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _searchBookingLiveData


    private val _bookingAcceptCancelLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val bookingAcceptCancelLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _bookingAcceptCancelLiveData

    private val _changePasswordLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val changePasswordLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _changePasswordLiveData


    private val _faqLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val faqLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _faqLiveData


    private val _aboutUsLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val aboutUsLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _aboutUsLiveData


    private val _privacyPolicyLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val privacyPolicyLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _privacyPolicyLiveData


    private val _chatLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val chatLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _chatLiveData


    private val _exitingPropertyDataLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val exitingPropertyDataLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _exitingPropertyDataLiveData


    private val _reviewLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val reviewLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _reviewLiveData


    private val _allChatMsgLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val allChatMsgLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _allChatMsgLiveData


    private val _discountCouponLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val discountCouponLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _discountCouponLiveData


    private val _updateDiscountCouponLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val updateDiscountCouponLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _updateDiscountCouponLiveData


    private val _allCouponsLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val allCouponsLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _allCouponsLiveData


    private val _dateBookingLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val dateBookingLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _dateBookingLiveData


    private val _documentIdLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val documentIdLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _documentIdLiveData


    private val _addReviewLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val addReviewLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _addReviewLiveData


    private val _notificationLiveData = MutableLiveData<NetworkResult<ResponseBody>?>()
    val notificationLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = _notificationLiveData



    suspend fun getUserProfileRepo(token:String) {
        _sellerDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getUserProfileApi(token)
        handleResponse(response)
    }




    suspend fun updateProfileRepo(
        token: RequestBody,
        userId: RequestBody,
        firstName: RequestBody,
        lastName: RequestBody,
        email: RequestBody,
        mobile: RequestBody,
        profilePicture: MultipartBody.Part
    ) {
        _sellerDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.updateProfileApi(token,userId,firstName,lastName,email,mobile,profilePicture)
        handleResponse(response)
    }



    suspend fun addPropertyRepo(
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

    ) {
        _sellerDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.addPropertyApi(token,userId, title, address, lat, long, description, bedrooms, guests, house, bathrooms, size, price, wifi, breakfast,pets,squreMeter, image)
        handleResponse(response)
    }




    suspend fun addMyPropertyRepo(
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

    ) {
        _sellerDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.addMyPropertyApi(token,userId,propertyCategoryId,rentId,rentStringName,lat,
            lon,address,bathroomCount,bedCount,bedroomCount,guestQuantity,selectFacilityId,selectAmenitiesId,
            selectSafetyId,title,description,requestAcceptTypeId,price,discountId,pets,squreMeter,image)
        handleResponse(response)
    }









    suspend fun getPropertyRepo(param:Map<String,String>) {
        _propertyDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getPropertyApi(param)
        if (response.isSuccessful && response.body() != null) {
            _propertyDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _propertyDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _propertyDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }




    suspend fun getPropertyCategoryRepo(token:String) {
        _sellerDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getPropertyCategoryApi(token)
        handleResponse(response)
    }


    suspend fun getPropertyPlaceRepo(token:String) {
        _sellerDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getPropertyPlaceApi(token)
        handleResponse(response)
    }

    suspend fun getTypeOfAmenitiesRepo(token: String) {
        _amenitiesDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getTypeOfAmenitiesApi(token)
        if (response.isSuccessful && response.body() != null) {
            _amenitiesDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _amenitiesDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _amenitiesDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }    }

    suspend fun getTypeOfFacilitiesRepo(token: String) {
        _facilitiesDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getTypeOfFacilityApi(token)
        if (response.isSuccessful && response.body() != null) {
            _facilitiesDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _facilitiesDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _facilitiesDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }    }

    suspend fun getTypeOfSafetyItemRepo(token: String) {
        _safetyDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getTypeOfSafetyItemsApi(token)
        if (response.isSuccessful && response.body() != null) {
            _safetyDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _safetyDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _safetyDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }    }




    suspend fun getTypeOfConfirmationModeRepo(token: String) {
        _sellerDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getTypeOfConfirmationModeApi(token)
        handleResponse(response)
    }



    suspend fun getAddedPropertyRepo(token: String) {
        _propertyAddedDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getAddedPropertyApi(token)
        if (response.isSuccessful && response.body() != null) {
            _propertyAddedDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _propertyAddedDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _propertyAddedDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }










    suspend fun getNearestPropertyPriceRepo(token:String,lat: String,lon :String) {
        _propertyDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getNearestPropertyPriceApi(token,lat,lon)
        if (response.isSuccessful && response.body() != null) {
            _propertyDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _propertyDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _propertyDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    suspend fun getCountryRepo(token: String) {
        _countryLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getCountryApi(token)
        if (response.isSuccessful && response.body() != null) {
            _countryLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _countryLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _countryLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    suspend fun getDocumentRepo(token: String,countryCode: String) {
        _documentLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getDocumentApi(token,countryCode)
        if (response.isSuccessful && response.body() != null) {
            _documentLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _documentLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _documentLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    suspend fun uploadDocumentRepo(
        token: RequestBody,
        userId: RequestBody,
        countryId: RequestBody,
        documentTypeId: RequestBody,
        image: MultipartBody.Part,
        image1: MultipartBody.Part,
        image2: MultipartBody.Part,
        image3: MultipartBody.Part


    ) {
        _sellerDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.uploadDocumentApi(token,userId,countryId,documentTypeId,image,image1,image2,image3)
        handleResponse(response)
    }


    suspend fun getPropertyDiscountRepo(token: String) {
        _documentLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getPropertyDiscountApi(token)
        if (response.isSuccessful && response.body() != null) {
            _documentLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _documentLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _documentLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    suspend fun getExitingPropertyRepo(token: String) {
        _exitingPropertyDataLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.exitingPropertyApi(token)
        if (response.isSuccessful && response.body() != null) {
            _exitingPropertyDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _exitingPropertyDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _exitingPropertyDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
















    private fun handleResponse(response: Response<ResponseBody>) {
        if (response.isSuccessful && response.body() != null) {
            _sellerDataLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _sellerDataLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _sellerDataLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    suspend fun getPropertyPriceWithDayRepo(token: String,propertyId: String) {
        _propertyPriceWithDayLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getPropertyPriceWithDayApi(token,propertyId)
        if (response.isSuccessful && response.body() != null) {
            _propertyPriceWithDayLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _propertyPriceWithDayLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _propertyPriceWithDayLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }




    suspend fun updatePropertyPriceRepo(param:Map<String,String>) {
        _updatePriceWithDayLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.updatePriceWithDayApi(param)
        if (response.isSuccessful && response.body() != null) {
            _updatePriceWithDayLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _updatePriceWithDayLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _updatePriceWithDayLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun updatePropertyDiscountRepo(param:Map<String,String>) {
        _updatePriceWithDayLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.updatePropertyDiscountApi(param)
        if (response.isSuccessful && response.body() != null) {
            _updatePriceWithDayLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _updatePriceWithDayLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _updatePriceWithDayLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    suspend fun getBookingRepo(param:Map<String,String>) {
        _bookingLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getBookingApi(param)
        if (response.isSuccessful && response.body() != null) {
            _bookingLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _bookingLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _bookingLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun searchBookingListDataRepo(param:Map<String,String>) {
        _searchBookingLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.searchBookingListDataApi(param)
        if (response.isSuccessful && response.body() != null) {
            _searchBookingLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _searchBookingLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _searchBookingLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }






    suspend fun bookingAcceptCancelRepo(param:Map<String,String>) {
        _bookingAcceptCancelLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.bookingAcceptCancelApi(param)
        if (response.isSuccessful && response.body() != null) {
            _bookingAcceptCancelLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _bookingAcceptCancelLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _bookingAcceptCancelLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun changePasswordRepo(map:Map<String,String>) {
        _changePasswordLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.changePasswordApi(map)
        if (response.isSuccessful && response.body() != null) {
            _changePasswordLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _changePasswordLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _changePasswordLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun faqRepo(token: String) {
        _faqLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.faqApi(token)
        if (response.isSuccessful && response.body() != null) {
            _faqLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _faqLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _faqLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun aboutUsRepo(token: String) {
        _aboutUsLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.aboutUsApi(token)
        if (response.isSuccessful && response.body() != null) {
            _aboutUsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _aboutUsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _aboutUsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun privacyPolicyRepo(token:String) {
        _privacyPolicyLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.privacyPolicyApi(token)
        if (response.isSuccessful && response.body() != null) {
            _privacyPolicyLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _privacyPolicyLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _privacyPolicyLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun sendNotificationChatRepo(map: Map<String, String>) {
        _chatLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.sendNotificationChatApi(map)
        if (response.isSuccessful && response.body() != null) {
            _chatLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _chatLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _chatLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun getReviewRepo(map:Map<String,String>) {
        _reviewLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getReviewApi(map)
        if (response.isSuccessful && response.body() != null) {
            _reviewLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _reviewLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _reviewLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun getReviewProviderRepo(map:Map<String,String>) {
        _reviewLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getReviewProviderApi(map)
        if (response.isSuccessful && response.body() != null) {
            _reviewLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _reviewLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _reviewLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }




    suspend fun allChatMsgRepo(map: Map<String, String>) {
        _allChatMsgLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.allChatMsgApi(map)
        if (response.isSuccessful && response.body() != null) {
            _allChatMsgLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _allChatMsgLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _allChatMsgLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun addDiscountCouponRepo(map: Map<String, String>) {
        _discountCouponLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.addDiscountCouponApi(map)
        if (response.isSuccessful && response.body() != null) {
            _discountCouponLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _discountCouponLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _discountCouponLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    suspend fun updateDiscountCouponRepo(map: Map<String, String>) {
        _updateDiscountCouponLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.updateDiscountCouponApi(map)
        if (response.isSuccessful && response.body() != null) {
            _updateDiscountCouponLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _updateDiscountCouponLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _updateDiscountCouponLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    suspend fun getAllCouponsRepo(token: String,propertyId:String) {
        _allCouponsLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getAllCouponApi(token,propertyId)
        if (response.isSuccessful && response.body() != null) {
            _allCouponsLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _allCouponsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _allCouponsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }




    suspend fun getBookingDetailsByDateRepo(map: Map<String, String>) {
        _dateBookingLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.getBookingDetailsByDateApi(map)
        if (response.isSuccessful && response.body() != null) {
            _dateBookingLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _dateBookingLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _dateBookingLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }



    suspend fun uploadDocumentIdRepo(map: Map<String, String>) {
        _documentIdLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.uploadDocumentIdApi(map)
        if (response.isSuccessful && response.body() != null) {
            _documentIdLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _documentIdLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _documentIdLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun addReviewRepo(map:Map<String,String>) {
        _addReviewLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.addReviewApi(map)
        if (response.isSuccessful && response.body() != null) {
            _addReviewLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _addReviewLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _addReviewLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun notificationRepo(map: Map<String, String>) {
        _notificationLiveData.postValue(NetworkResult.Loading())
        val response = sellerDataApi.notificationApi(map)
        if (response.isSuccessful && response.body() != null) {
            _notificationLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notificationLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _notificationLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }





    fun clearData() {
        _sellerDataLiveData.value = null
    }


    fun clearProperData() {
        _propertyDataLiveData.value = null
    }


    fun clearAmenitiesData() {
        _amenitiesDataLiveData.value = null
    }

    fun clearFacilitiesData() {
        _facilitiesDataLiveData.value = null
    }

    fun clearSafetyData() {
        _safetyDataLiveData.value = null
    }


    fun clearAddedProperData() {
        _propertyAddedDataLiveData.value = null
    }

    fun clearDocumentData() {
        _documentLiveData.value = null
    }


    fun clearCountryData() {
        _countryLiveData.value = null
    }


    fun clearPriceData() {
        _propertyPriceWithDayLiveData.value = null
    }

    fun clearUpdatePriceData() {
        _updatePriceWithDayLiveData.value = null
    }

    fun clearBookingData() {
        _bookingLiveData.value = null
    }

    fun clearBookingAcceptCancelData() {
        _bookingAcceptCancelLiveData.value = null
    }

    fun clearChangePasswordData() {
        _changePasswordLiveData.value = null
    }


    fun clearFaqData() {
        _faqLiveData.value = null
    }

    fun clearAboutUsData() {
        _aboutUsLiveData.value = null
    }

    fun clearPrivacyPolicyData() {
        _privacyPolicyLiveData.value = null
    }

    fun clearChatData() {
        _chatLiveData.value = null
    }

    fun clearExitingPropertyData() {
        _exitingPropertyDataLiveData.value = null
    }


    fun clearReviewData() {
        _reviewLiveData.value = null
    }

    fun clearAllChatMsgData() {
        _allChatMsgLiveData.value = null
    }


    fun clearSearchBookingListData() {
        _searchBookingLiveData.value = null
    }


    fun clearDiscountCouponData() {
        _discountCouponLiveData.value = null
    }


    fun clearUpdateDiscountCouponData() {
        _updateDiscountCouponLiveData.value = null
    }


    fun clearAllCouponsData() {
        _allCouponsLiveData.value = null
    }

    fun clearDateBookingData() {
        _dateBookingLiveData.value = null
    }


    fun clearDocumentIdData() {
        _documentIdLiveData.value = null
    }

    fun clearAddReviewData() {
        _addReviewLiveData.value = null
    }

    fun clearNotificationData() {
        _notificationLiveData.value = null
    }


}