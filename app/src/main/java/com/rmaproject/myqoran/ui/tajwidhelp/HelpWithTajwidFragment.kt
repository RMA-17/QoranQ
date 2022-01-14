package com.rmaproject.myqoran.ui.tajwidhelp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.control.RepeatMode
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.TajweedLayoutBinding
import com.rmaproject.myqoran.ui.settings.QariPreferences
import com.rmaproject.myqoran.ui.tajwid.QuranArabicUtils


class HelpWithTajwidFragment : Fragment(R.layout.tajweed_layout) {

    private val binding:TajweedLayoutBinding by viewBinding()
    //IsOpened checker
    private var isIdghamOpened = false
    private var isIkhfaOpened = false
    private var isIqlabOpened = false
    private var isQalqalaOpened = false
    private var isGhunnahOpened = false
    private var isIdghamBilaghunnahOpened = false

    private var urlNamaQari = ""
    private var artist = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*Initialisation*/

        //Card Hider
        binding.tajweedInfoIdghamBilaGunnah.visibility = View.GONE
        binding.tajweedInfoIdgham.visibility = View.GONE
        binding.tajweedInfoGunnah.visibility = View.GONE
        binding.tajweedInfoQalqala.visibility = View.GONE
        binding.tajweedInfoIqlab.visibility = View.GONE
        binding.tajweedInfoIkhfa.visibility = View.GONE

        //Qari Selection
        when (QariPreferences(context).qariPreference) {
            0 -> {
                urlNamaQari = "Mishary%20Rashid%20Alafasy"
                artist = "Mishary Rashid Alafasy"
            }
            1 -> {
                urlNamaQari = "Muhammad%20Ayyoub%2064kbps"
                artist = "Muhammad Ayyoub"
            }
            2 -> {
                urlNamaQari = "Abdurrahman%20as-Sudais"
                artist = "Abdurrahman As-Sudais"
            }
            3 -> {
                urlNamaQari = "Hudhaify%2064kbps"
                artist = "Hudaify"
            }
        }

        //Arabic Example
        val ghunnahEx = "إِلَٰهِ ٱلنَّاسِ ٣"
        val idghamBighunnahEx = "بَلَىٰ قَٰدِرِينَ عَلَىٰٓ أَن نُّسَوِّيَ بَنَانَهُۥ ٤"
        val idghamBilaghunnahEx = "وَلَمۡ يَكُن لَّهُۥ كُفُوًا أَحَدُۢ ٤"
        val iqlabEx = "كـَلَّاۖ لَيُنۢبَذَنَّ فِي ٱلۡحُطَمَةِ ٤"
        val ikhfaEx = "ٱلَّذِيٓ أَنقَضَ ظَهۡرَكَ ٣"
        val qalqalaEx = "سَيَصۡلَىٰ نَارٗا ذَاتَ لَهَبٖ ٣"

        //Animation
        val showCard = AnimationUtils.loadAnimation(requireContext(), R.anim.show_card_anim)
        val hideCard = AnimationUtils.loadAnimation(requireContext(), R.anim.hide_card_anim)

        /*Action when upper card is clicked*/

        //Idhgam
        binding.cardIdghamBighunnah.setOnClickListener {
            if (isIdghamOpened) {
                binding.tajweedInfoIdgham.startAnimation(hideCard)
                binding.tajweedInfoIdgham.visibility = View.GONE
                isIdghamOpened = false
            } else if (!isIdghamOpened) {
                isIdghamOpened = true
                binding.tajweedInfoIdgham.startAnimation(showCard)
                binding.tajweedInfoIdgham.visibility = View.VISIBLE
            }
        }

        //Ikhfa
        binding.cardIkhfa.setOnClickListener {
            if (isIkhfaOpened) {
                binding.tajweedInfoIkhfa.startAnimation(hideCard)
                binding.tajweedInfoIkhfa.visibility = View.GONE
                isIkhfaOpened = false
            } else if (!isIkhfaOpened) {
                binding.tajweedInfoIkhfa.startAnimation(showCard)
                isIkhfaOpened = true
                binding.tajweedInfoIkhfa.visibility = View.VISIBLE
            }
        }

        //Iqlab
        binding.cardIqlab.setOnClickListener {
            if (isIqlabOpened) {
                binding.tajweedInfoIqlab.startAnimation(hideCard)
                binding.tajweedInfoIqlab.visibility = View.GONE
                isIqlabOpened = false
            } else if (!isIqlabOpened) {
                binding.tajweedInfoIqlab.startAnimation(showCard)
                isIqlabOpened = true
                binding.tajweedInfoIqlab.visibility = View.VISIBLE
            }
        }

        //Qalqalah
        binding.cardQalqala.setOnClickListener {
            if (isQalqalaOpened) {
                binding.tajweedInfoQalqala.startAnimation(hideCard)
                binding.tajweedInfoQalqala.visibility = View.GONE
                isQalqalaOpened = false
            } else if (!isQalqalaOpened) {
                binding.tajweedInfoQalqala.startAnimation(showCard)
                isQalqalaOpened = true
                binding.tajweedInfoQalqala.visibility = View.VISIBLE
            }
        }

        //Ghunnah
        binding.cardGunnah.setOnClickListener {
            if (isGhunnahOpened) {
                binding.tajweedInfoGunnah.startAnimation(hideCard)
                binding.tajweedInfoGunnah.visibility = View.GONE
                isGhunnahOpened = false
            } else if (!isGhunnahOpened) {
                binding.tajweedInfoGunnah.startAnimation(showCard)
                isGhunnahOpened = true
                binding.tajweedInfoGunnah.visibility = View.VISIBLE
            }
        }

        //Idgham Bilaghunnah
        binding.cardIdghamBilaghunnah.setOnClickListener {
            if (isIdghamBilaghunnahOpened) {
                binding.tajweedInfoIdghamBilaGunnah.startAnimation(hideCard)
                binding.tajweedInfoIdghamBilaGunnah.visibility = View.GONE
                isIdghamBilaghunnahOpened = false
            } else if (!isIdghamBilaghunnahOpened) {
                isIdghamBilaghunnahOpened = true
                binding.tajweedInfoIdghamBilaGunnah.startAnimation(showCard)
                binding.tajweedInfoIdghamBilaGunnah.visibility = View.VISIBLE
            }
        }

        /*Action that will happen inside the card*/
        coloringTajweed(requireContext(), ghunnahEx, idghamBighunnahEx, idghamBilaghunnahEx, qalqalaEx, iqlabEx, ikhfaEx)

        /*Play Ayah Click*/

        //Play Idgham Bighunnah
        binding.teksArabIdghamBi.setOnClickListener {
            playTajweedExample(urlNamaQari, 40, 4)
        }
        //Play Ikhfa
        binding.teksIkhfa.setOnClickListener {
            playTajweedExample(urlNamaQari, 94, 3)
        }
        //Play Iqlab
        binding.teksIqlab.setOnClickListener {
            playTajweedExample(urlNamaQari, 104, 4)
        }
        //Play Qalqalah
        binding.teksQalqalah.setOnClickListener {
            playTajweedExample(urlNamaQari, 111, 3)
        }
        //Play Ghunnah
        binding.teksGunnah.setOnClickListener {
            playTajweedExample(urlNamaQari, 114, 3)
        }
        //Play Idgham Bilaghunnah
        binding.teksIdghamBl.setOnClickListener {
            playTajweedExample(urlNamaQari, 112, 4)
        }
    }

    private fun coloringTajweed(context: Context, ghunnahExample:String, idghamBighunnahExample:String, idghamBilaghunnahExample:String, qalqalaExample:String, iqlabExample:String, ikhfaExample:String) {
        binding.teksGunnah.text = QuranArabicUtils.getTajweed(context, ghunnahExample)
        binding.teksArabIdghamBi.text = QuranArabicUtils.getTajweed(context, idghamBighunnahExample)
        binding.teksIkhfa.text = QuranArabicUtils.getTajweed(context, ikhfaExample)
        binding.teksIdghamBl.text = QuranArabicUtils.getTajweed(context, idghamBilaghunnahExample)
        binding.teksQalqalah.text = QuranArabicUtils.getTajweed(context, qalqalaExample)
        binding.teksIqlab.text = QuranArabicUtils.getTajweed(context, iqlabExample)
    }

    private fun playTajweedExample(artist:String, surahNumber:Int, ayahNumber:Int) {
        val audio = SongInfo()
        val formatSurahNumber = String.format("%03d", surahNumber)
        val formatAyahNumber = String.format("%03d", ayahNumber)
        val url = "https://archive.org/download/quran-every-ayah/$artist.zip/$formatSurahNumber$formatAyahNumber.mp3"
        audio.songUrl = url
        audio.artist = artist
        audio.songCover = "https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/10/04/676590316.jpg"
        audio.songId = "$surahNumber$ayahNumber"
        audio.songName = "Tajwid Example"
        StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_NONE, false)
        StarrySky.with().playMusicByInfo(audio)
    }
}