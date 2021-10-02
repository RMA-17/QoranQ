package com.rmaproject.myqoran.ui.quran.indexby.page

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
/*
            Code writen by Raka M.A
             */
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ItemviewquranbypageBinding
import com.rmaproject.myqoran.model.AyahTerakhirFinder
import com.rmaproject.myqoran.model.Page
import com.rmaproject.myqoran.ui.quran.indexby.page.QuranIndexAdapterPage.QuranIndexViewHolder

class QuranIndexAdapterPage(val pageList:List<Page>, val ayahTerakhir:List<AyahTerakhirFinder>,
                            val clickListener:(Page) -> Unit
) :
    RecyclerView.Adapter<QuranIndexViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranIndexViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemviewquranbypage, parent, false)
        return QuranIndexViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranIndexViewHolder, position: Int) {
        val page = pageList[position]
        val ayahterakhir = ayahTerakhir[position]
        holder.bindingView(page, ayahterakhir)
        holder.binding.clickableLayout.setOnClickListener {
            clickListener.invoke(page)
        }
    }

    override fun getItemCount(): Int {
        return pageList.size
    }

    class QuranIndexViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val binding:ItemviewquranbypageBinding by viewBinding()
        fun bindingView(page:Page, ayahTerakhir:AyahTerakhirFinder) {
            binding.pageCounter.text = page.page.toString()
            binding.halamanLocator.text = "Halaman ${page.page.toString()}"
            binding.firstsurahLine.text = "${page.SurahName_en}: ${page.AyahNumber}"
            binding.lastSurahLine.text = "${ayahTerakhir.SurahName_en}: ${ayahTerakhir.AyahNumber}"

        }

    }
}