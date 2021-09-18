package com.rmaproject.myqoran.ui.quran.indexby.page

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ItemviewquranbypageBinding
import com.rmaproject.myqoran.model.Page
import com.rmaproject.myqoran.model.Surah
import com.rmaproject.myqoran.ui.quran.indexby.page.QuranIndexAdapterPage.QuranIndexViewHolder

class QuranIndexAdapterPage(val pageList:List<Page>,
                            val clickListener:(Page) -> Unit
) :
    RecyclerView.Adapter<QuranIndexViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranIndexViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemviewquranbypage, parent, false)
        return QuranIndexViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranIndexViewHolder, position: Int) {
        val page = pageList[position]
        holder.bindingView(page)
        holder.binding.clickableLayout.setOnClickListener {
            clickListener.invoke(page)
        }
    }

    override fun getItemCount(): Int {
        return pageList.size
    }

    class QuranIndexViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val binding:ItemviewquranbypageBinding by viewBinding()
        fun bindingView(page:Page) {
            binding.pageCounter.text = page.page.toString()
            binding.halamanLocator.text = "Halaman ${page.page.toString()}"
            binding.firstsurahLine.text = "${page.SurahName_en}"
            binding.lastSurahLine.text = page.TextQuran

        }

    }
}