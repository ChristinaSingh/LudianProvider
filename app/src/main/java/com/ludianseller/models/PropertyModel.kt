package com.ludianseller.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PropertyModel(
   /* val message: String,
    @SerializedName("properties") val properties: List<Property>,
    val status: String*/

    val properties: List<Property>,
    val message: String,
    val status: String

) : Serializable{
    data class Property(
        @SerializedName("property_id") val propertyId: String,
        val title: String,
        val bedrooms: String,
        val guests: String,
        val house: String,
        val bathrooms: String,
        val price: String,
        val wifi: String,
        val breakfast: String,
        val size: String,
        val description: String,
        @SerializedName("user_id") val userId: String,
        val address: String,
        val lat: String,
        val long: String,
        @SerializedName("property_images") val propertyImages: List<PropertyImage>
    ) : Serializable {
        data class PropertyImage(
            val id: String,
            @SerializedName("property_id") val propertyId: String,
            val image: String
        ) : Serializable
    }
}