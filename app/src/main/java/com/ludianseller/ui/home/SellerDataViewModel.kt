package com.ludianseller.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludianseller.R
import com.ludianseller.repository.SellerDataRepository
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class SellerDataViewModel @Inject constructor(private val sellerDataRepository: SellerDataRepository) :
    ViewModel() {

    val sellerResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.sellerResponseLiveData



    val propertyResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.propertyResponseLiveData


    val amenitiesResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.amenitiesResponseLiveData

    val facilityResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.facilitiesResponseLiveData

    val safetyResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.safetyResponseLiveData



    val propertyAddedDataLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.propertyAddedDataLiveData


    val countryLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.countryLiveData

    val documentLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.documentLiveData


    val propertyPriceWithDayLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.propertyPriceWithDayLiveData


    val updatePriceWithDayLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.updatePriceWithDayLiveData

    val bookingLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.bookingLiveData

    val bookingAcceptCancelLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.bookingAcceptCancelLiveData

    val chatLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.chatLiveData

    val reviewResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.reviewLiveData

    val allChatMsgLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.allChatMsgLiveData


    val searchBookingLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.searchBookingLiveData

    val discountCouponLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.discountCouponLiveData


    val updateDiscountCouponLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.updateDiscountCouponLiveData


    val allCouponsLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.allCouponsLiveData


    val dateBookingLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.dateBookingLiveData


    val documentIdLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.documentIdLiveData


    val addReviewResponseLiveData: LiveData<NetworkResult<ResponseBody>?>
        get() = sellerDataRepository.addReviewLiveData



    fun getProperty(token: String) {
        viewModelScope.launch {
            sellerDataRepository.getAddedPropertyRepo(token)
        }
    }


    fun getUserProfile(token:String) {
        viewModelScope.launch {
            sellerDataRepository.getUserProfileRepo(token)
        }
    }

    fun updateProfile(
        token: RequestBody,
        userId: RequestBody,
        firstName: RequestBody,
        lastName: RequestBody,
        email: RequestBody,
        mobile: RequestBody,
        profilePicture: MultipartBody.Part
    ) {
        viewModelScope.launch {
            sellerDataRepository.updateProfileRepo(
                token,
                userId,
                firstName,
                lastName,
                email,
                mobile,
                profilePicture
            )
        }
    }


    fun getProperty(param: Map<String, String>) {
        viewModelScope.launch {
            sellerDataRepository.getPropertyRepo(param)
        }
    }


    fun getPropertyCategory(token:String) {
        viewModelScope.launch {
            sellerDataRepository.getPropertyCategoryRepo(token)
        }
    }



    fun getPropertyPlace(token: String) {
        viewModelScope.launch {
            sellerDataRepository.getPropertyPlaceRepo(token)
        }
    }


    fun getTypeOfAmenities(token: String) {
        viewModelScope.launch {
            sellerDataRepository.getTypeOfAmenitiesRepo(token)
        }
    }


    fun getTypeOfFacilities(token: String) {
        viewModelScope.launch {
            sellerDataRepository.getTypeOfFacilitiesRepo(token)
        }
    }


    fun getTypeOfSafetyItem(token: String) {
        viewModelScope.launch {
            sellerDataRepository.getTypeOfSafetyItemRepo(token)
        }
    }


    fun getTypeOfConfirmationMode(token: String) {
        viewModelScope.launch {
            sellerDataRepository.getTypeOfConfirmationModeRepo(token)
        }
    }


    fun getNearestPropertyPrice(token:String,lat :String,lon :String) {
        viewModelScope.launch {
            sellerDataRepository.getNearestPropertyPriceRepo(token,lat,lon)
        }
    }

    fun getCountry(token: String) {
        viewModelScope.launch {
            sellerDataRepository.getCountryRepo(token)
        }
    }


    fun getDocumentRepo(token: String,countryCode: String) {
        viewModelScope.launch {
            sellerDataRepository.getDocumentRepo(token,countryCode)
        }
    }



    fun uploadDocument(
        token: RequestBody,
        userId: RequestBody,
        countryId: RequestBody,
        documentTypeId: RequestBody,
        image: MultipartBody.Part,
        image1: MultipartBody.Part,
        image2: MultipartBody.Part,
        image3: MultipartBody.Part

    ) {
        viewModelScope.launch {
            sellerDataRepository.uploadDocumentRepo(
               token,
                userId,
                countryId,
                documentTypeId,
                image,image1,image2,image3
            )
        }
    }


    fun getPropertyDiscount(token: String) {
        viewModelScope.launch {
            sellerDataRepository.getPropertyDiscountRepo(token)
        }
    }


    fun getPropertyPriceWithDay(token: String,propertyId: String) {
        viewModelScope.launch {
            sellerDataRepository.getPropertyPriceWithDayRepo(token,propertyId)
        }
    }


    fun updatePropertyPrice(params : Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.updatePropertyPriceRepo(params)
        }
    }


    fun updatePropertyDiscount(params : Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.updatePropertyDiscountRepo(params)
        }
    }


    fun getBooking(map:Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.getBookingRepo(map)
        }
    }

    fun searchBookingListData(map:Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.searchBookingListDataRepo(map)
        }
    }




    fun bookingAcceptCancel(map:Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.bookingAcceptCancelRepo(map)
        }
    }

    fun sendNotificationChat(map:Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.sendNotificationChatRepo(map)
        }
    }

    fun getReviews(map: Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.getReviewRepo(map)
        }
    }

    fun getReviewsProvider(map: Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.getReviewProviderRepo(map)
        }
    }



    fun allChatMsg(map:Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.allChatMsgRepo(map)
        }
    }


    fun addDiscountCoupon(map:Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.addDiscountCouponRepo(map)
        }
    }


    fun updateDiscountCoupon(map:Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.updateDiscountCouponRepo(map)
        }
    }


    fun getAllCoupons(token: String,propertyId:String){
        viewModelScope.launch {
            sellerDataRepository.getAllCouponsRepo(token,propertyId)
        }
    }


    fun getBookingDetailsByDate(map:Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.getBookingDetailsByDateRepo(map)
        }
    }


    fun uploadDocumentIdData(map:Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.uploadDocumentIdRepo(map)
        }
    }


    fun addReview(map: Map<String,String>){
        viewModelScope.launch {
            sellerDataRepository.addReviewRepo(map)
        }
    }



    fun clearAddPropertyData() {
        sellerDataRepository.clearAddedProperData()
    }

    fun clearData() {
        sellerDataRepository.clearData()
    }


    fun clearPropertyData() {
        sellerDataRepository.clearProperData()
    }

    fun clearAmenitiesData() {
        sellerDataRepository.clearAmenitiesData()
    }

    fun clearFacilitiesData() {
        sellerDataRepository.clearFacilitiesData()
    }

    fun clearSafetyData() {
        sellerDataRepository.clearSafetyData()
    }


    fun clearDocumentData() {
        sellerDataRepository.clearDocumentData()

    }


    fun clearCountryData() {
        sellerDataRepository.clearCountryData()

    }


    fun clearPriceData() {
        sellerDataRepository.clearPriceData()
    }

    fun clearUpdatePriceData() {
        sellerDataRepository.clearUpdatePriceData()
    }



    fun clearBookingData() {
        sellerDataRepository.clearBookingData()

    }

    fun clearBookingAcceptCancelData() {
        sellerDataRepository.clearBookingAcceptCancelData()
    }


    fun clearChatData() {
        sellerDataRepository.clearChatData()
    }


    fun clearReviewData() {
        sellerDataRepository.clearReviewData()
    }


    fun clearAllChatMsgData() {
        sellerDataRepository.clearAllChatMsgData()
    }

    fun clearSearchBookingListData() {
        sellerDataRepository.clearSearchBookingListData()

    }


    fun clearDiscountCouponData() {
        sellerDataRepository.clearDiscountCouponData()
    }


    fun clearUpdateDiscountCouponData() {
        sellerDataRepository.clearUpdateDiscountCouponData()
    }

    fun clearAllCouponsData() {
        sellerDataRepository.clearAllCouponsData()

    }

    fun clearDateBookingData() {
        sellerDataRepository.clearDateBookingData()
    }

    fun clearDocumentIdData() {
        sellerDataRepository.clearDocumentIdData()

    }
    fun clearAddReviewData() {
        sellerDataRepository.clearAddReviewData()
    }



    fun validateProfile(
        context: Context, fName: String, lName: String, emailAddress: String, mobile: String
    ): Pair<Boolean, String> {

        var result = Pair(true, "")

        if (fName == "") {
            result = Pair(false, context.getString(R.string.please_enter_first_name))

        } else if (lName == "") {
            result = Pair(false, context.getString(R.string.please_enter_last_name))
        } else if (emailAddress == "") {
            result = Pair(false, context.getString(R.string.please_enter_email))
        } else if (!Helper.isValidEmail(emailAddress)) {
            result = Pair(false, context.getString(R.string.email_is_invalid))
        } else if (mobile == "") {
            result = Pair(false, context.getString(R.string.please_enter_mobile_number))

        }

        return result

    }











}