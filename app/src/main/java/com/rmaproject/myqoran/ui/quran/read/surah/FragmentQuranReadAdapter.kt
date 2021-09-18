package com.rmaproject.myqoran.ui.quran.read.surah

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ItemAyatBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranReadAdapter.QuranReadViewHolder

class FragmentQuranReadAdapter(val ayatList:List<Quran>, val totalAyah:Int) : RecyclerView.Adapter<QuranReadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranReadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ayat, parent, false)
        return QuranReadViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranReadViewHolder, position: Int) {
        val ayat = ayatList[position]
        holder.bindView(ayat, totalAyah)

    }

    override fun getItemCount(): Int {
        return ayatList.size
    }

    class QuranReadViewHolder(itemview:View) : RecyclerView.ViewHolder(itemview){
        val binding : ItemAyatBinding by viewBinding()
        fun bindView(ayatList:Quran, totalAyah: Int){
            binding.ayahCounter.text = ayatList.AyahNumber.toString()
            binding.ayatQuran.text = ayatList.TextQuran
            binding.namaSurat.text = ayatList.SurahName_en
            binding.translate.text = ayatList.translation
            binding.CardView.isVisible = ayatList.AyahNumber == 1
            binding.ayatTotal.text = "${totalAyah} Ayat"
        }
    }

}