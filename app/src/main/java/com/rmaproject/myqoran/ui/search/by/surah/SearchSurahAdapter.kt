package com.rmaproject.myqoran.ui.search.by.surah

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ItemviewSearchsurahBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.model.Surah

class SearchSurahAdapter (val surahList:List<Quran>,
                          val totalAyah:List<Int>,
                          val clickListener:(Quran) -> Unit // <- Callback
) : // Titik 2 = inheritance, koma = implement
    RecyclerView.Adapter<SearchSurahAdapter.QuranSearchSurahViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): QuranSearchSurahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemview_searchsurah, parent, false)
        return QuranSearchSurahViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranSearchSurahViewHolder, position: Int, ) {
        val surah = surahList[position]
        holder.bindView(surah) // holderSurah mengambil bindview nya dari bawah. Lalu mengirimkan List kedalam binview, setelah itu List nya diatur oleh postitiopn
        val surahNumber : Int = surah.surahNumber ?: 1
        val totalAyah = totalAyah[surahNumber - 1]
        holder.binding.clickableLayout.setOnClickListener{
            clickListener.invoke(surah)
        }
        holder.binding.totalAyat.text = "$totalAyah Ayat"
    }

    override fun getItemCount(): Int {
        return surahList.size
    }
    class QuranSearchSurahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemviewSearchsurahBinding by viewBinding()

        fun bindView(surah: Quran) {
            binding.surahlocator.text = surah.surahNumber.toString()
            binding.surahnameEN.text = surah.SurahName_en

        }

    }
}