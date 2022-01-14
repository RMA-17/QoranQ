package com.rmaproject.myqoran.network.`interface`

import com.google.gson.Gson
import com.rmaproject.myqoran.network.model.PrayerTimeModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PrayerTimeAPI {

    @GET("today.json")

    suspend fun getPrayerTime(
        @Query("longitude") longitude:Double,
        @Query("latitude") latitude:Double
    ): PrayerTimeModel
    
    companion object {
        private const val BASE_URL = "https://api.pray.zone/v2/times/"
        fun create(): PrayerTimeAPI {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
                .create(PrayerTimeAPI::class.java)
        }
    }

}