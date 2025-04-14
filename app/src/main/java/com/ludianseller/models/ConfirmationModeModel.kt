package com.ludianseller.models

import com.google.gson.annotations.SerializedName

data class ConfirmationModeModel(
    val result: List<Property>,
    val message: String,
    val status: Int
) {
    data class Property(
        @SerializedName("type_of_confirmation_mode_id") val id: String,
        @SerializedName("type_of_confirmation_mode_name") val name: String,
        @SerializedName("type_of_confirmation_mode_description") val description: String,
        @SerializedName("type_of_confirmation_mode_image") val image: String,
        @SerializedName("type_of_confirmation_mode_admin_status") val adminStatus: String,

        var check: Boolean = false

    )
}