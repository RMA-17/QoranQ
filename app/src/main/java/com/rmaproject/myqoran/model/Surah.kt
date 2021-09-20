package com.rmaproject.myqoran.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.PrimaryKey


@DatabaseView("SELECT sora, sora_name_ar, sora_name_en, COUNT(id) as ayah_total FROM quran GROUP by sora")

data class Surah(

    @PrimaryKey val id:Int? = 0,
    @ColumnInfo(name = "sora") val surahNumber:Int? = 0,
    @ColumnInfo(name = "sora_name_ar") val surahNameArabic:String? = "",
    @ColumnInfo(name = "sora_name_en") val surahNameEN:String? = "",
    @ColumnInfo(name = "ayah_total") val numberOfAyah:Int? = 0

)
