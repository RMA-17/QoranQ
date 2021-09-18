package com.rmaproject.myqoran.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran") //Entity = Model dari database
data class Quran(
    @PrimaryKey val id : Int? = 0,
    @ColumnInfo(name = "jozz") val juzNumber : Int?,
    @ColumnInfo(name = "sora") val surahNumber : Int? = 0, //Surah Number
    @ColumnInfo(name = "sora_name_en") val SurahName_en : String? = "",
    @ColumnInfo(name = "sora_name_ar") val SurahName_ar : String? = "",
    @ColumnInfo(name = "page") val page : Int? = 0,
    @ColumnInfo(name = "line_start") val lineStart: Int? = 0,
    @ColumnInfo(name = "line_end") val lineEnd:Int? = 0,
    @ColumnInfo(name = "aya_no") val AyahNumber : Int? = 0,
    @ColumnInfo(name = "aya_text") val TextQuran: String? = "",
    @ColumnInfo(name = "aya_text_emlaey") val textQuranSearch:String? = "",
    val translation:String? = "",
    val footnotes:String? = ""
)

//ColumnInfo = memberi sebuah info/deskripsi kepada sebuah table