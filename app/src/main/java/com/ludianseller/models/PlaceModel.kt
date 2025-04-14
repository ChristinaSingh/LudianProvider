package com.ludianseller.models

import com.google.gson.annotations.SerializedName



data class PlaceModel(
    val result: List<Property>,
    val message: String,
    val status: Int
) {
    data class Property(
        @SerializedName("type_of_place_id") val id: String,
        @SerializedName("type_of_place_name") val name: String,
        @SerializedName("type_of_place_description") val description: String,
        @SerializedName("type_of_place_image") val image: String,
        @SerializedName("type_of_place_admin_status") val adminStatus: String,

        var check: Boolean = false

    )
}
