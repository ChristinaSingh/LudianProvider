package com.ludianseller.models

import com.google.gson.annotations.SerializedName

data class PlaceTypeModel(
    val result: List<Property>,
    val message: String,
    val status: Int
) {
    data class Property(
        @SerializedName("property_category_id") val propertyCategoryId: String,
        @SerializedName("property_category_name") val propertyCategoryName: String,
        @SerializedName("property_category_name_ar") val propertyCategoryNameAr: String,
        @SerializedName("property_category_image") val propertyCategoryImage: String,
        @SerializedName("property_category_admin_status") val propertyCategoryAdminStatus: String,
        var check: Boolean = false

    )
}
