package com.ludianseller.models

import com.google.gson.annotations.SerializedName


data class DocumentTypeModel(

    @SerializedName("data")
    val result: List<Documents>,
    val message: String,
    val status: Int
) {
    data class Documents(
        @SerializedName("document_id") val id: String,
        @SerializedName("document_name") val name: String,
        @SerializedName("country_code") val countryCode: String,
        @SerializedName("document_code") val documentCode: String,
        var check: Boolean = false

    )
}