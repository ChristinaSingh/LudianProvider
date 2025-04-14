package com.ludianseller.ui.calendar.model

import com.google.gson.annotations.SerializedName

data class CouponModel(
    val result: List<Result>,
    val message: String,
    val status: Int

){
    data class Result(
        @SerializedName("id") val id: String,
        @SerializedName("seller_id") val sellerId: String,
        @SerializedName("property_id") val propertyId: String,
        @SerializedName("title") val title: String,
        @SerializedName("discount_percent") val discountPercent: String,
        @SerializedName("start_date") val startDate: String,
        @SerializedName("end_date") val endDate: String,





        )
}
