package com.ludianseller.models

import com.google.gson.annotations.SerializedName



data class SafetyModel(
    val result: List<Property>,
    val message: String,
    val status: Int
) {
    data class Property(
        @SerializedName("type_of_safety_item_id") val id: String,
        @SerializedName("type_of_safety_item_name") val name: String,
        @SerializedName("type_of_safety_item_name_ar") val nameAr: String,
        @SerializedName("type_of_safety_item_image") val image: String,
        @SerializedName("type_of_safety_item_admin_status") val adminStatus: String,
        var check: Boolean = false

    )
}