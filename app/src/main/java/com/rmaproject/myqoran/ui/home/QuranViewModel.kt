package com.rmaproject.myqoran.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmaproject.myqoran.model.Surah
import java.text.FieldPosition

class QuranViewModel : ViewModel() {

    private val positionTab : MutableLiveData<Int> = MutableLiveData()
    private val totalAyahs:MutableLiveData<List<Int>> = MutableLiveData()

    fun setPositionTab(position: Int){
        positionTab.value = position
    }

    fun getPositionTab() = positionTab

    fun setTotalAyahList(surahList:List<Surah>){
        val listTotalAyahs = mutableListOf<Int>()
        surahList.forEach{
            val numberOfAyah = it.numberOfAyah ?: 1
            listTotalAyahs.add(numberOfAyah)
        }
        totalAyahs.value = listTotalAyahs
    }

    fun getTotalAyahList() = totalAyahs
}