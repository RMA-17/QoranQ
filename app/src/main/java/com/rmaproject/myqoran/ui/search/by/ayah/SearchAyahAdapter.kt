package com.rmaproject.myqoran.ui.search.by.ayah

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.color.MaterialColors
import com.l4digital.fastscroll.FastScroller
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ItemviewSearchayahBinding
import com.rmaproject.myqoran.model.Bookmark
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.ui.settings.FontSizePreferences
import com.rmaproject.myqoran.ui.tajwid.QuranArabicUtils
import java.text.DateFormat
import java.util.*
import java.util.regex.Pattern

class SearchAyahAdapter(private val ayatList: List<Quran>) : RecyclerView.Adapter<SearchAyahAdapter.SearchAyahViewHolder>(), FastScroller.SectionIndexer {

    var shareOnclickListener:((Quran, Int) -> Unit)? = null
    var copyOnclickListener:((Quran, Int) -> Unit)? = null
    var footNoteOnClickListener:((Quran) -> Unit)? = null
    var bookMarkClickListener:((Bookmark) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAyahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemview_searchayah, parent, false)
        return SearchAyahViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAyahViewHolder, position: Int) {
        val context = holder.itemView.context
        val ayat = ayatList[position]
        val surahNumber : Int = ayat.surahNumber ?: 1
//        val totalAyah = totalAyah[surahNumber - 1]
        holder.footNoteTracker(holder.itemView, ayat, footNoteOnClickListener)
//        holder.translateSearchTracker(holder.itemView, ayat, query)
        holder.bindView(ayat)

//        when (FontSizePreferences(context).gantiFontSize){
//            35 -> {
//                holder.binding.ayatQuran.textSize = 35F
//            }
//            40 -> {
//                holder.binding.ayatQuran.textSize = 40F
//            }
//            50 -> {
//                holder.binding.ayatQuran.textSize = 50F
//            }
//        }
        holder.binding.ayatQuran.textSize = FontSizePreferences(context).gantiFontSize.toFloat()
        holder.binding.itemShare.setOnClickListener{
            shareOnclickListener?.invoke(ayat, position)
        }
        holder.binding.ayatQuran.text = holder.reverseAyahNumber(ayat, context)
        holder.binding.itemCopy.setOnClickListener {
            copyOnclickListener?.invoke(ayat, position)
        }
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance().format(calendar.time)
        holder.binding.saveLastReadBookmark.setOnClickListener {
            val saveBookmark = Bookmark(id = null,
            surahName = ayatList[position].SurahName_en,
            ayatNumber = ayatList[position].AyahNumber,
            surahNumber = ayatList[position].surahNumber,
            positionScroll = ayatList[position].AyahNumber!! -1,
            timeStamp = currentDate,
            timeAdded = Date().time
            )
            Toast.makeText(context, "Surah ${ayatList[position].SurahName_en}: ${ayatList[position].AyahNumber} berhasil ditambahkan kedalam bookmark", Toast.LENGTH_SHORT).show()
            bookMarkClickListener?.invoke(saveBookmark)
        }
    }

    override fun getItemCount(): Int {
        return ayatList.size
    }

    class SearchAyahViewHolder (itemview: View) : RecyclerView.ViewHolder(itemview) {
        val binding : ItemviewSearchayahBinding by viewBinding()

        fun bindView(ayatList:Quran){
            binding.ayahCounter.text = ayatList.AyahNumber.toString()
            binding.namaSurat.text = ayatList.SurahName_en

        }

        fun reverseAyahNumber(quran: Quran, context: Context): Spannable {
            val digit = mutableListOf<Char>()
            quran.TextQuran!!.forEach{ char ->
                if (char.isDigit())
                    digit.add(char)
            }
            val ayahNumberArabic = digit.joinToString("")
            val textWithoutNumber = quran.TextQuran.replace(ayahNumberArabic, "")
            val textWithNumber = textWithoutNumber + digit.reversed().joinToString("")
            return QuranArabicUtils.getTajweed(context, textWithNumber)
        }

        fun translateSearchTracker (view: View, quran:Quran, query:String) {
            val colorPrimary = MaterialColors.getColor(view, R.attr.colorPrimary)
            val translate = quran.translation
            val spannable = SpannableStringBuilder(translate)
            val pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(translate)
            spannable.setSpan(
                ForegroundColorSpan(colorPrimary), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.translate.setText(spannable, TextView.BufferType.SPANNABLE)

        }

        fun footNoteTracker(view: View, quran:Quran, footNoteonClickListener: ((Quran) -> Unit)?) {
            val colorPrimary = MaterialColors.getColor(view, R.attr.colorPrimary)
            val translate = quran.translation
            val spannable = SpannableStringBuilder(translate)
            val pattern = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(translate)
            while (matcher.find()){
                val clickableOpen = object :ClickableSpan() {
                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = colorPrimary
                        ds.isUnderlineText = true
                    }

                    override fun onClick(p0: View) {
                        footNoteonClickListener?.invoke(quran)
                    }
                }
                spannable.setSpan(clickableOpen, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                spannable.setSpan(RelativeSizeSpan(0.8F), matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                spannable.setSpan(SuperscriptSpan(), matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
            binding.translate.movementMethod = LinkMovementMethod.getInstance()
            binding.translate.setText(spannable, TextView.BufferType.SPANNABLE)
            /*
            Code writen by Raka M.A
             */
        }

        companion object{
            const val SurahAtTaubah = 9
            const val SurahAlFatihah = 1
        }

    }

    override fun getSectionText(position: Int): CharSequence {
        val namasurat =  ayatList[position].SurahName_en
        val ayat = ayatList[position].AyahNumber
        return "$namasurat: $ayat"
    }

}
