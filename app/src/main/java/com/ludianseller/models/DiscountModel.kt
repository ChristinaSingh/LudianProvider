package com.ludianseller.models

import com.google.gson.annotations.SerializedName


data class DiscountModel(

    val result: List<Discount>,
    val message: String,
    val status: Int
) {
    data class Discount(
        @SerializedName("property_discount_id") val id: String,
        @SerializedName("property_discount_title") val title: String,
        @SerializedName("property_discount_description") val subTitle: String,
        @SerializedName("property_discount_value") val discount: String,
        var check: Boolean = false

    )
}