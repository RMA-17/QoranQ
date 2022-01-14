package com.rmaproject.myqoran.network.model


import com.google.gson.annotations.SerializedName

data class PrayerTimeModel(
    @SerializedName("code")
    val code: Int,
    @SerializedName("results")
    val results: Results,
    @SerializedName("status")
    val status: String
)