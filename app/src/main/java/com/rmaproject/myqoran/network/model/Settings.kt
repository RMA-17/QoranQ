package com.rmaproject.myqoran.network.model


import com.google.gson.annotations.SerializedName

data class Settings(
    @SerializedName("fajr_angle")
    val fajrAngle: Double,
    @SerializedName("highlat")
    val highlat: String,
    @SerializedName("isha_angle")
    val ishaAngle: Double,
    @SerializedName("juristic")
    val juristic: String,
    @SerializedName("school")
    val school: String,
    @SerializedName("timeformat")
    val timeformat: String
)