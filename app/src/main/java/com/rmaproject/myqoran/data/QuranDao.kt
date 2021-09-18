package com.rmaproject.myqoran.data

import androidx.room.Dao
import androidx.room.Query
import com.rmaproject.myqoran.model.Jozz
import com.rmaproject.myqoran.model.Page
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.model.Surah
import kotlinx.coroutines.flow.Flow

@Dao //Data access object = Untuk menyimpan query SQL
interface QuranDao {

    @Query("SELECT * FROM Surah")
    fun showQuranIndexBySurah():Flow<List<Surah>>

    @Query("SELECT * FROM Jozz")
    fun showQuranIndexByJozz():Flow<List<Jozz>>

    @Query("SELECT * FROM Page")
    fun showQuranIndexByPage():Flow<List<Page>>

    @Query("SELECT sora, jozz, aya_no, aya_text, aya_text_emlaey, translation, sora_name_ar, sora_name_en, footnotes FROM quran WHERE sora = :surahNumber")
    fun readQuranBySurah(surahNumber:Int):Flow<List<Quran>>

    @Query("SELECT sora, jozz, aya_no, aya_text, aya_text_emlaey, translation, sora_name_ar, sora_name_en, footnotes FROM quran WHERE jozz = :jozzNumber")
    fun readQuranByJozz(jozzNumber:Int):Flow<List<Quran>>

    @Query("SELECT page, sora, jozz, aya_no, aya_text, aya_text_emlaey, translation, sora_name_ar, sora_name_en, footnotes FROM quran WHERE page = :pageNumber")
    fun readQUranByPage(pageNumber:Int):Flow<List<Quran>>

}