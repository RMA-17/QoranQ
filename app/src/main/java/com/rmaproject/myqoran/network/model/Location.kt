package com.rmaproject.myqoran.network.model


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("country")
    val country: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("elevation")
    val elevation: Double,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("local_offset")
    val localOffset: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("timezone")
    val timezone: String
)