package com.rmaproject.myqoran.ui.quran.indexby.jozz
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
/*
            Code writen by Raka M.A
             */
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.l4digital.fastscroll.FastScroller
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ItemviewquranbyjozzBinding
import com.rmaproject.myqoran.model.Jozz
import com.rmaproject.myqoran.ui.quran.indexby.jozz.QuranIndexAdapterJozz.QuranIndexJozzViewHolder

class QuranIndexAdapterJozz(val jozzList:List<Jozz>, val clickListener:(Jozz) -> Unit): RecyclerView.Adapter<QuranIndexJozzViewHolder>(), FastScroller.SectionIndexer {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranIndexJozzViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemviewquranbyjozz, parent, false)
        return QuranIndexJozzViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranIndexJozzViewHolder, position: Int) {
        val jozz = jozzList[position]
        holder.bindview(jozz)
        holder.binding.clickableLayout.setOnClickListener{
            clickListener.invoke(jozz)
        }
    }

    override fun getItemCount(): Int {
        return jozzList.size
    }


    class QuranIndexJozzViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding:ItemviewquranbyjozzBinding by viewBinding()
        fun bindview(jozz:Jozz){
            binding.surahNameEN.text = jozz.SurahName_en
            binding.namaJuz.text = "Juz ${jozz.juzNumber}"
            binding.ayaText.text = jozz.TextQuran
            binding.jozzcounter.text = jozz.juzNumber.toString()
            binding.ayahNumberTxt.text = jozz.nomorAyah.toString()
        }

    }

    override fun getSectionText(position: Int): CharSequence {
        val jozz = jozzList[position]
        val popupText = "Juz ${jozz.juzNumber}"
        return popupText
    }
}