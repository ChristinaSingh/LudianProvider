package com.ludianseller.models

import com.google.gson.annotations.SerializedName

data class CountryModel(

    @SerializedName("data")
    val result: List<Country>,
    val message: String,
    val status: Int
) {
    data class Country(
        @SerializedName("id") val id: String,
        @SerializedName("iso") val iso: String,
        @SerializedName("name") val name: String,
        @SerializedName("nicename") val nicename: String,

        @SerializedName("iso3") val iso3: String,
        @SerializedName("numcode") val numcode: String,
        @SerializedName("phonecode") val phonecode: String,
        )
}