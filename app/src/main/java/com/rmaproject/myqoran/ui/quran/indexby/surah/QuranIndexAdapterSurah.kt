package com.rmaproject.myqoran.ui.quran.indexby.surah

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ItemviewquranbysurahBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.model.Surah

class QuranIndexAdapterSurah(val surahList:List<Surah>,
                             val clickListener:(Surah) -> Unit // <- Callback
) :
    RecyclerView.Adapter<QuranIndexAdapterSurah.QuranIndexSurahViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranIndexSurahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemviewquranbysurah, parent, false)
        return QuranIndexSurahViewHolder(view)
    }

    override fun onBindViewHolder(holderSurah: QuranIndexSurahViewHolder, position: Int) {
        val surah = surahList[position]
        holderSurah.bindView(surah) // holderSurah mengambil bindview nya dari bawah. Lalu mengirimkan List kedalam binview, setelah itu List nya diatur oleh postitiopn
        holderSurah.binding.clickableLayout.setOnClickListener{
            clickListener.invoke(surah)
        }
    }

    override fun getItemCount(): Int {
        return surahList.size
    }

    class QuranIndexSurahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val binding:ItemviewquranbysurahBinding by viewBinding()

        fun bindView(surah: Surah) {
            binding.surahNameArabic.text = surah.surahNameArabic
            binding.surahlocator.text = surah.surahNumber.toString()
            binding.surahnameEN.text = surah.surahNameEN
            binding.ayatTotal.text = "${surah.numberOfAyah.toString()} Ayah"
            binding.turunDi.isVisible = false
            binding.dots.isVisible = false

        }

    }

}