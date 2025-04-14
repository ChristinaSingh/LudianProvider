package com.ludianseller.ui.calendar.model

import com.google.gson.annotations.SerializedName

data class CheckPriceModel(
    val result: List<Property>,
  /*  @SerializedName("discount_detail") val discount: Discount,*/
    val message: String,
    val status: Int
) {
    data class Property(
        @SerializedName("date_availability_id") val id: String,
        @SerializedName("date_availability_date") val date: String,
        @SerializedName("date_availability_price") val price: String,
        @SerializedName("date_availability_property_id") val propertyId: String,
        @SerializedName("date_availability_status") val avlStatus: String,
        @SerializedName("date_availability_note") val note: String,

    )

/*
    data class Discount(
        @SerializedName("id") val id: String,
        @SerializedName("title") val title: String,
        @SerializedName("start_date") val startDate: String,
        @SerializedName("end_date") val endDate: String,
        @SerializedName("discount_percent") val discount: String,

        )
*/

}