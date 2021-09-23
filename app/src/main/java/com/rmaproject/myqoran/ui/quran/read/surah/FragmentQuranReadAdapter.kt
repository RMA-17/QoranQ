package com.rmaproject.myqoran.ui.quran.read.surah

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.color.MaterialColors
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ItemAyatBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranReadAdapter.QuranReadViewHolder
import java.util.regex.Pattern

class FragmentQuranReadAdapter(private val ayatList:List<Quran>,
                               private val totalAyah:List<Int>) : RecyclerView.Adapter<QuranReadViewHolder>() {

    var shareOnclickListener:((Quran, Int) -> Unit)? = null
    var copyOnclickListener:((Quran, Int) -> Unit)? = null
    var footNoteOnClickListener:((Quran) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranReadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ayat, parent, false)
        return QuranReadViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranReadViewHolder, position: Int) {
        val ayat = ayatList[position]
        val surahNumber : Int = ayat.surahNumber ?: 1
        val totalAyah = totalAyah[surahNumber - 1]
        holder.footNoteTracker(holder.itemView, ayat, footNoteOnClickListener)
        holder.bindView(ayat, totalAyah, position)

        holder.binding.itemShare.setOnClickListener{
            shareOnclickListener?.invoke(ayat, position)
        }
        holder.binding.itemCopy.setOnClickListener {
            copyOnclickListener?.invoke(ayat, position)
        }

    }

    override fun getItemCount(): Int {
        return ayatList.size
    }

    class QuranReadViewHolder(itemview:View) : RecyclerView.ViewHolder(itemview){
        val binding : ItemAyatBinding by viewBinding()
        fun bindView(ayatList:Quran, totalAyah: Int, position: Int) = binding.apply {
            ayahCounter.text = ayatList.AyahNumber.toString()
            ayatQuran.text = reverseAyahNumber(ayatList.TextQuran.toString())
            namaSurat.text = ayatList.SurahName_en
            CardView.isVisible = ayatList.AyahNumber == 1
            ayatTotal.text = "${totalAyah} Ayat"
            artiSurat.text = ayatList.SurahName_id
            turunDi.text = ayatList.turunSurah


            if (ayatList.turunSurah == "Meccan"){
                turunDi.text = "Mekkah"
            } else if (ayatList.turunSurah == "Madinan") {
                turunDi.text = "Madinah"
            }
            if (ayatList.surahNumber == SurahAlFatihah){
                binding.bismillah.text = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ"
                binding.bismillah.textSize = 35F
            } else if(ayatList.surahNumber == SurahAtTaubah){
                binding.bismillah.text = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ"
                binding.bismillah.textSize = 35F
            }
            else {
                binding.bismillah.text = "بِسْمِ ٱللَّٰهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ"
            }
        }

        fun reverseAyahNumber(textQuran: String): String {
            val digit = mutableListOf<Char>()
            textQuran.forEach { char ->
                if (char.isDigit())
                    digit.add(char)
            }
            val ayahNumberArabic = digit.joinToString("")
            val textWithoutNumber = textQuran.replace(ayahNumberArabic, "")
            return textWithoutNumber + digit.reversed().joinToString("")
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
            }
            binding.translate.movementMethod = LinkMovementMethod.getInstance()
            binding.translate.setText(spannable, TextView.BufferType.SPANNABLE)

        }
    }

    companion object{
        const val SurahAtTaubah = 9
        const val SurahAlFatihah = 1
    }
}