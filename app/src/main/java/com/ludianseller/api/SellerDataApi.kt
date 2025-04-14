package com.ludianseller.api

import com.ludianseller.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface SellerDataApi {

    @GET(Constants.GET_USER_PROFILE_API)
    suspend fun getUserProfileApi(@Query("token") token:String): Response<ResponseBody>


    @Multipart
    @POST(Constants.UPDATE_PROFILE_API)
    suspend fun updateProfileApi(
        @Part("token") token: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("first_name") firstName: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part profilePicture: MultipartBody.Part
    ): Response<ResponseBody>


    @Multipart
    @POST(Constants.ADD_PROPERTY_API)
    suspend fun addPropertyApi(
        @Part("token") token: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("address") address: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("long") long: RequestBody,
        @Part("description") description: RequestBody,
        @Part("bedrooms") bedrooms: RequestBody,
        @Part("guests") guests: RequestBody,
        @Part("house") house: RequestBody,
        @Part("bathrooms") bathrooms: RequestBody,
        @Part("size") size: RequestBody,
        @Part("price") price: RequestBody,
        @Part("wifi") wifi: RequestBody,
        @Part("breakfast") breakfast: RequestBody,
        @Part("pets") pets: RequestBody,
        @Part("squreMeter") squreMeter: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<ResponseBody>



    @Multipart
    @POST(Constants.ADD_MY_PROPERTY_API)
    suspend fun addMyPropertyApi(
        @Part("token") token: RequestBody,
        @Part("id") userId: RequestBody,
        @Part("propertyCategoryId") propertyCategoryId: RequestBody,
        @Part("rentId") rentId: RequestBody,
        @Part("rentStringName") rentStringName: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
        @Part("address") address: RequestBody,
        @Part("bathroomCount") bathroomCount: RequestBody,
        @Part("bedCount") bedCount: RequestBody,
        @Part("bedroomCount") bedroomCount: RequestBody,
        @Part("guestQuantity") guestQuantity: RequestBody,
        @Part("selectFacilityId") selectFacilityId: RequestBody,
        @Part("selectAmenitiesId") selectAmenitiesId: RequestBody,
        @Part("selectSafetyId") selectSafetyId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("requestAcceptTypeId") requestAcceptTypeId: RequestBody,
        @Part("price") price: RequestBody,
        @Part("discount_id")discountId:RequestBody,
        @Part("pets")pets:RequestBody,
        @Part("squreMeter")squreMeter:RequestBody,
        @Part images: List<MultipartBody.Part>

    ): Response<ResponseBody>




    @FormUrlEncoded
    @POST(Constants.GET_PROPERTY_API)
    suspend fun getPropertyApi(@FieldMap params: Map<String, String>): Response<ResponseBody>




    @GET(Constants.GET_PROPERTY_CATEGORY_API)
    suspend fun getPropertyCategoryApi(@Query("token") token:String): Response<ResponseBody>


    @GET(Constants.GET_PROPERTY_PLACE_API)
    suspend fun getPropertyPlaceApi(@Query("token") token:String): Response<ResponseBody>


    @GET(Constants.GET_TYPE_OF_AMENITIES_API)
    suspend fun getTypeOfAmenitiesApi(@Query("token") token:String): Response<ResponseBody>


    @GET(Constants.GET_TYPE_OF_FACILITIES_API)
    suspend fun getTypeOfFacilityApi( @Query("token") token:String): Response<ResponseBody>


    @GET(Constants.GET_TYPE_OF_SAFETY_ITEMS_API)
    suspend fun getTypeOfSafetyItemsApi( @Query("token") token:String): Response<ResponseBody>


    @GET(Constants.GET_TYPE_OF_CONFIRMATION_MODE_API)
    suspend fun getTypeOfConfirmationModeApi( @Query("token") token:String): Response<ResponseBody>





    @GET(Constants.GET_NEAREST_PROPERTY_PRICE_API)
    suspend fun getNearestPropertyPriceApi(@Query("token") token:String,
        @Query("latitude") latitude: String,
                                             @Query("longitude") longitude: String): Response<ResponseBody>



    @GET(Constants.GET_ADDED_PROPERTY_API)
    suspend fun getAddedPropertyApi(@Query("token") token:String): Response<ResponseBody>


    @GET(Constants.GET_COUNTRY_API)
    suspend fun getCountryApi(@Query("token") token:String): Response<ResponseBody>

    @GET(Constants.GET_DOCUMENT_API)
    suspend fun getDocumentApi(@Query("token") token:String,@Query("country_code") countryCode: String): Response<ResponseBody>



    @Multipart
    @POST(Constants.UPLOAD_DOCUMENT_API)
    suspend fun uploadDocumentApi(
        @Part("token") token: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part("countryId") countryId: RequestBody,
        @Part("documentTypeID") documentTypeId: RequestBody,
        @Part image: MultipartBody.Part,
        @Part image1: MultipartBody.Part,
        @Part image2: MultipartBody.Part,
        @Part image3: MultipartBody.Part

    ): Response<ResponseBody>

    @GET(Constants.GET_PROPERTY_DISCOUNT_API)
    suspend fun getPropertyDiscountApi(@Query("token") token:String): Response<ResponseBody>

    @GET(Constants.GET_PROPERTY_PRICE_WITH_DAY_API)
    suspend fun getPropertyPriceWithDayApi(@Query("token") token:String,@Query("property_id") propertyId: String): Response<ResponseBody>

    @FormUrlEncoded
    @POST(Constants.UPDATE_PRICE_API)
    suspend fun updatePriceWithDayApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.UPDATE_PROPERTY_DISCOUNT_API)
    suspend fun updatePropertyDiscountApi(@FieldMap params: Map<String, String>): Response<ResponseBody>

    @FormUrlEncoded
    @POST(Constants.GET_BOOKING_API)
    suspend fun getBookingApi(@FieldMap params: Map<String, String>): Response<ResponseBody>

    @FormUrlEncoded
    @POST(Constants.SEARCH_BOOKING_LIST_DATA_API)
    suspend fun searchBookingListDataApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.BOOKING_ACCEPT_CANCEL_API)
    suspend fun bookingAcceptCancelApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.PASSWORD_CHANGE_API)
    suspend fun changePasswordApi(@FieldMap params: Map<String, String>): Response<ResponseBody>

    @GET(Constants.PRIVACY_POLICY_API)
    suspend fun privacyPolicyApi(@Query("token") token:String): Response<ResponseBody>

    @GET(Constants.GET_FAQ_API)
    suspend fun faqApi(@Query("token") token:String): Response<ResponseBody>

    @GET(Constants.ABOUT_US_API)
    suspend fun aboutUsApi(@Query("token") token:String): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.CHAT_NOTIFICATION_API)
    suspend fun sendNotificationChatApi(@FieldMap params: Map<String, String>): Response<ResponseBody>

    @GET(Constants.EXITING_PROPERTY_API)
    suspend fun exitingPropertyApi(@Query("token") token:String): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.GET_PROPERTY_REVIEW_API)
    suspend fun getReviewProviderApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.GET_USER_ALL_REVIEW_API)
    suspend fun getReviewApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.ALL_CHAT_MSG_API)
    suspend fun allChatMsgApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.ADD_DISCOUNT_COUPON_API)
    suspend fun addDiscountCouponApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.UPDATE_DISCOUNT_COUPON_API)
    suspend fun updateDiscountCouponApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @GET(Constants.GET_ALL_COUPON_API)
    suspend fun getAllCouponApi(@Query("token") token: String,@Query("property_id") propertyId: String): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.GET_BOOKING_DETAILS_BY_API)
    suspend fun getBookingDetailsByDateApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.UPLOAD_DOCUMENT_ID_API)
    suspend fun uploadDocumentIdApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.ADD_PROPERTY_REVIEW_API)
    suspend fun addReviewApi(@FieldMap params: Map<String, String>): Response<ResponseBody>


    @FormUrlEncoded
    @POST(Constants.NOTIFICATION_API)
    suspend fun notificationApi(@FieldMap params: Map<String, String>): Response<ResponseBody>

}