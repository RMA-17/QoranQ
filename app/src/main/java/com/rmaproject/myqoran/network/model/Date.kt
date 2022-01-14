package com.rmaproject.myqoran.network.model


import com.google.gson.annotations.SerializedName

data class Date(
    @SerializedName("gregorian")
    val gregorian: String,
    @SerializedName("hijri")
    val hijri: String,
    @SerializedName("timestamp")
    val timestamp: Int
)