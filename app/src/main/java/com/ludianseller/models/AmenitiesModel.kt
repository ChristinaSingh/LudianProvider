package com.ludianseller.models

import com.google.gson.annotations.SerializedName



data class AmenitiesModel(
    val result: List<Property>,
    val message: String,
    val status: Int
) {
    data class Property(
        @SerializedName("type_of_amenity_id") val id: String,
        @SerializedName("type_of_amenity_name") val name: String,
        @SerializedName("type_of_amenity_name_ar") val nameAr: String,
        @SerializedName("type_of_amenity_image") val image: String,
        @SerializedName("type_of_amenity_admin_status") val adminStatus: String,
        var check: Boolean = false

    )
}