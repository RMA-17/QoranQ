package com.rmaproject.myqoran.ui.quran.read.surah

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import com.l4digital.fastscroll.FastScroller
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ItemAyatBinding
import com.rmaproject.myqoran.model.Bookmark
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.ui.home.QuranViewModel
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranReadAdapter.QuranReadViewHolder
import com.rmaproject.myqoran.ui.settings.FocusOnReadModePreferences
import com.rmaproject.myqoran.ui.settings.FontSizePreferences
import com.rmaproject.myqoran.ui.settings.LastReadPreferences
import com.rmaproject.myqoran.ui.tajwid.QuranArabicUtils
import java.text.DateFormat
import java.util.*
import java.util.regex.Pattern


class FragmentQuranReadAdapter(
    private val ayatList: List<Quran>,
    private val totalAyah: List<Int>,var viewModel: QuranViewModel
) : RecyclerView.Adapter<QuranReadViewHolder>(), FastScroller.SectionIndexer {

    var shareOnclickListener:((Quran, Int) -> Unit)? = null
    var copyOnclickListener:((Quran, Int) -> Unit)? = null
    var footNoteOnClickListener:((Quran) -> Unit)? = null
    var playMurottalListener:((Quran, Int) -> Unit)? = null
    var playAllAyahClickListener:((Quran, Int) -> Unit)? = null
    var bookMarkClickListener:((Bookmark) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranReadViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ayat, parent, false)
        return QuranReadViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranReadViewHolder, position: Int) {
        val context = holder.itemView.context
        val ayat = ayatList[position]
        val surahNumber : Int = ayat.surahNumber ?: 1
        val totalAyah = totalAyah[surahNumber - 1]
        val positionee = ayatList[position].AyahNumber!! -1

        val slide2leftBm = AnimationUtils.loadAnimation(context, R.anim.geser_ke_kiri_bookmark)
        val slide2leftBmrvs = AnimationUtils.loadAnimation(context, R.anim.geser_ke_kiri_bookmark_reverse)
        val slide2leftshr = AnimationUtils.loadAnimation(context, R.anim.geser_ke_kiri_share)
        val slide2leftshrrvs = AnimationUtils.loadAnimation(context, R.anim.geser_ke_kiri_share_reverse)
        val slide2leftplay = AnimationUtils.loadAnimation(context, R.anim.geser_ke_kiri_play)
        val slide2leftplayrvs = AnimationUtils.loadAnimation(context, R.anim.geser_ke_kiri_play_reverse)
        val slide2leftcopy = AnimationUtils.loadAnimation(context, R.anim.geser_ke_kiri_copy)
        val slide2leftcopyrvs = AnimationUtils.loadAnimation(context, R.anim.geser_ke_kiri_copy_reverse)

        var isMenuOpened:Boolean = false

        holder.footNoteTracker(holder.itemView, ayat, footNoteOnClickListener)
        holder.bindView(ayat, totalAyah, context)

        LastReadPreferences(context).lastReadSurah = "Surah " + ayatList[position].SurahName_en
        LastReadPreferences(context).lastReadAyah = "Ayah no. " + ayatList[position].AyahNumber
        LastReadPreferences(context).lastReadPosition = ayatList[position].AyahNumber!! -1
        LastReadPreferences(context).lastReadSurahNumber = ayatList[position].surahNumber!!

        viewModel.setCurrentSurah(ayatList[position].SurahName_en!!, ayatList[position].AyahNumber!!)

        Log.d("SURAHNUMBER", ayatList[position].toString())
        Log.d("POSITION", positionee.toString())

        holder.binding.ayatQuran.textSize = FontSizePreferences(context).gantiFontSize.toFloat()

        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance().format(calendar.time)
        holder.binding.saveLastReadBookmark.setOnClickListener {
            val saveBookmark = Bookmark (id = null,
                surahName = ayatList[position].SurahName_en,
                ayatNumber = ayatList[position].AyahNumber,
                surahNumber = ayatList[position].surahNumber,
                positionScroll = ayatList[position].AyahNumber!! -1,
                timeStamp = currentDate,
                textQuran = ayatList[position].TextQuran,
                timeAdded = Date().time
            )

            Snackbar.make(holder.itemView, "${ayatList[position].SurahName_en}: ${ayatList[position].AyahNumber} ditambahkan kedalam bookmark", Snackbar.LENGTH_SHORT)
                .setTextColor(MaterialColors.getColor(holder.itemView, R.attr.colorPrimary))
                .setAnimationMode(ANIMATION_MODE_SLIDE)
                .setAction("Ok") {
                    // Kalau di kotlin ini tidak usah di isi untuk dismiss saja.
                }
                .setActionTextColor(MaterialColors.getColor(holder.itemView, R.attr.colorPrimary))
                .setBackgroundTint(MaterialColors.getColor(holder.itemView, R.attr.primary_50))
                .show()

            bookMarkClickListener?.invoke(saveBookmark)
        }

        holder.binding.playMurottal.setOnClickListener {
            playMurottalListener?.invoke(ayat, totalAyah)
        }

        holder.binding.playAllAyah.setOnClickListener {
            playAllAyahClickListener?.invoke(ayat, totalAyah)
        }

        holder.binding.itemShare.setOnClickListener{
            shareOnclickListener?.invoke(ayat, position)
        }

        holder.binding.itemCopy.setOnClickListener {
            copyOnclickListener?.invoke(ayat, position)
        }

        holder.binding.ayatQuran.text = holder.reverseAyahNumber(ayatList[position], context)
        holder.binding.moreOptionsBtn.setOnClickListener {
            if (!isMenuOpened){
                holder.binding.moreOptionsBtn.animate().apply {
                    duration = 500
                    rotation(-360f)
                }.withStartAction {
                    holder.binding.saveLastReadBookmark.isVisible = true
                    holder.binding.saveLastReadBookmark.startAnimation(slide2leftBm)
                    holder.binding.itemShare.isVisible = true
                    holder.binding.itemShare.startAnimation(slide2leftshr)
                    holder.binding.playMurottal.isVisible = true
                    holder.binding.playMurottal.startAnimation(slide2leftplay)
                    holder.binding.itemCopy.isVisible = true
                    holder.binding.itemCopy.startAnimation(slide2leftcopy)
                }.withEndAction {
                    isMenuOpened = true
                }
            } else {
                holder.binding.moreOptionsBtn.animate().apply {
                    duration = 500
                    rotation(360f)
                }.withStartAction {
                    holder.binding.saveLastReadBookmark.startAnimation(slide2leftBmrvs)
                    holder.binding.saveLastReadBookmark.visibility = View.INVISIBLE
                    holder.binding.itemShare.startAnimation(slide2leftshrrvs)
                    holder.binding.itemShare.visibility = View.INVISIBLE
                    holder.binding.playMurottal.startAnimation(slide2leftplayrvs)
                    holder.binding.playMurottal.visibility = View.INVISIBLE
                    holder.binding.itemCopy.startAnimation(slide2leftcopyrvs)
                    holder.binding.itemCopy.visibility = View.INVISIBLE
                }.withEndAction {
                    isMenuOpened = false
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return ayatList.size
    }

    class QuranReadViewHolder(itemview:View) : RecyclerView.ViewHolder(itemview){
        val binding : ItemAyatBinding by viewBinding()

        fun bindView(ayatList: Quran, totalAyah: Int, context: Context) = binding.apply {
            ayahCounter.text = ayatList.AyahNumber.toString()
            namaSurat.text = ayatList.SurahName_en
            CardView.isVisible = ayatList.AyahNumber == 1
            ayatTotal.text = "${totalAyah} Ayat"
            artiSurat.text = ayatList.SurahName_id
            turunDi.text = ayatList.turunSurah
            playAllAyah.isVisible = ayatList.AyahNumber == 1

            if (ayatList.turunSurah == "Meccan"){
                turunDi.text = "Mekkah"
            } else if (ayatList.turunSurah == "Madinan") {
                turunDi.text = "Madinah"
            }

            when (ayatList.surahNumber) {
                SurahAlFatihah -> {
                    binding.bismillah.text = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ"
                    binding.bismillah.textSize = 35F
                }
                SurahAtTaubah -> {
                    binding.bismillah.text = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ"
                    binding.bismillah.textSize = 35F
                }
                else -> {
                    binding.bismillah.text = "بِسْمِ ٱللَّٰهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ"
                }
            }

            if (FocusOnReadModePreferences(context).foModeOn) {
                binding.toolBarAyah.isVisible = false
                binding.translate.isVisible = false
                binding.divider.isVisible = true
            } else {
                binding.toolBarAyah.isVisible = true
                binding.translate.isVisible = true
                binding.divider.isVisible = false
            }
        }

        fun reverseAyahNumber(quran:Quran, context: Context): Spannable {
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
    }

    companion object{
        const val SurahAtTaubah = 9
        const val SurahAlFatihah = 1
    }

    override fun getSectionText(position: Int): CharSequence {
        return "${ayatList[position].SurahName_en}: ${ayatList[position].AyahNumber.toString()}"
    }
}