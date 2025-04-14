package com.ludianseller.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CalenderPropertyModel(
    @SerializedName("properties")
    val properties: List<Property>,
    val message: String,
    val status: Int
) : Serializable {
    data class Property(
        @SerializedName("property_id")
        val propertyId: String,
        val title: String,
        val bedrooms: String,
        val guests: String,
        val bathrooms: String,
        val price: String,
        val description: String,
        @SerializedName("user_id")
        val userId: String,
        val address: String,
        val longitude: String,
        val latitude: String,
        @SerializedName("property_category_id")
        val propertyCategoryId: String,
        @SerializedName("rent_id")
        val rentId: String,
        @SerializedName("rent_string_name")
        val rentStringName: String,
        @SerializedName("bed_count")
        val bedCount: String,
        @SerializedName("select_facility_ids")
        val selectFacilityIds: String,
        @SerializedName("select_amenities_ids")
        val selectAmenitiesIds: String,
        @SerializedName("select_safety_ids")
        val selectSafetyIds: String,
        @SerializedName("request_accept_type_id")
        val requestAcceptTypeId: String,
        @SerializedName("image_urls")
        val imageUrls: List<String>,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("admin_status")
        val adminStatus: String,
        @SerializedName("approval_status")
        val approvalStatus: String,
        val ratting_avg: String,
        val squreMeter: String,
        val wifi: Boolean

        ) : Serializable
}