package com.rmaproject.myqoran.data

import androidx.room.*
import com.rmaproject.myqoran.model.*
import kotlinx.coroutines.flow.Flow

@Dao //Data access object = Untuk menyimpan query SQL
interface QuranDao {

    @Query("SELECT * FROM Surah")
    fun showQuranIndexBySurah():Flow<List<Surah>>

    @Query("SELECT * FROM quran WHERE translation LIKE :search OR aya_text_emlaey LIKE :search")
    fun searchEntireQuran(search:String):Flow<List<Quran>>

    @Query("SELECT * FROM quran WHERE sora_name_emlaey LIKE :searchSurah OR sora = :searchSurah GROUP BY sora")
    fun searchSurah(searchSurah: String) : Flow<List<Quran>>

    @Query("SELECT * FROM quran WHERE sora = :surahNumber AND translation LIKE :search OR aya_text_emlaey LIKE :search")
    fun searchReadSurah(search:String, surahNumber: Int):Flow<List<Quran>>

    @Query("SELECT * FROM quran WHERE jozz = :jozzNumber AND translation LIKE :search OR aya_text_emlaey LIKE :search")
    fun searchReadJozz(search:String, jozzNumber: Int):Flow<List<Quran>>

    @Query("SELECT * FROM quran WHERE page = :pageNumber AND translation LIKE :search OR aya_text_emlaey LIKE :search")
    fun searchReadPage(search:String, pageNumber: Int):Flow<List<Quran>>

    @Query("SELECT * FROM AyahTerakhirFinder")
    fun showAyahterakhir():Flow<List<AyahTerakhirFinder>>

    @Query("SELECT * FROM Jozz")
    fun showQuranIndexByJozz():Flow<List<Jozz>>

    @Query("SELECT * FROM Page")
    fun showQuranIndexByPage():Flow<List<Page>>

    @Query("SELECT sora, jozz, aya_no, aya_text, aya_text_emlaey, translation, sora_name_ar, sora_name_en, sora_name_id,sora_descend_place, footnotes FROM quran WHERE sora = :surahNumber")
    fun readQuranBySurah(surahNumber:Int):Flow<List<Quran>>

    @Query("SELECT sora, jozz, aya_no, aya_text, aya_text_emlaey, translation, sora_name_ar, sora_name_en, sora_name_id, sora_descend_place, footnotes FROM quran WHERE jozz = :jozzNumber")
    fun readQuranByJozz(jozzNumber:Int):Flow<List<Quran>>

    @Query("SELECT page, sora, jozz, aya_no, aya_text, aya_text_emlaey, translation, sora_name_ar, sora_name_en, sora_name_id, sora_descend_place, footnotes FROM quran WHERE page = :pageNumber")
    fun readQUranByPage(pageNumber:Int):Flow<List<Quran>>



}