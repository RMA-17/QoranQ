package com.rmaproject.myqoran.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
/*
            Code writen by Raka M.A
             */
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentHomeBinding
import com.rmaproject.myqoran.ui.quran.indexby.jozz.QuranIndexFragmentByJozz
import com.rmaproject.myqoran.ui.quran.indexby.page.QuranIndexFragmentByPage
import com.rmaproject.myqoran.ui.quran.indexby.surah.QuranIndexFragmentBySurah
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranRead
import com.rmaproject.myqoran.ui.settings.LastReadPreferences
import java.util.*
import java.util.Calendar

import com.rmaproject.myqoran.ui.settings.ThemePreferences


class HomeFragment : Fragment(R.layout.fragment_home) {

    //ViewBinding kita tidak harus mengetik findViewById.
    private val bindings: FragmentHomeBinding by viewBinding()
    private val titles: List<String> = listOf("Surah", "Juz", "Page")
    private val fragmentList: List<Fragment> = listOf(QuranIndexFragmentBySurah(),
        QuranIndexFragmentByJozz(),
        QuranIndexFragmentByPage()) //List Fragment yang bakal bisa digeser
    private val viewModel: QuranViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        when (ThemePreferences(context).darkMode) {
            1 -> {
                bindings.imageHeader.load(R.drawable.night_background) {
                    crossfade(true)
                }
            }
            0 -> {
                bindings.imageHeader.load(R.drawable.light_header) {
                    crossfade(true)
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val en = Locale.ENGLISH
        val namaHari = arrayListOf("Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Minggu", "Unknown Day")
        val cal = UmmalquraCalendar(en)
        val tanggalHijriyyah =
            cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
        val tanggalanHijriyyah = "${namaHari[dayConvertertoId(tanggalHijriyyah)]}, ${cal[Calendar.DAY_OF_MONTH]} ${
            cal.getDisplayName(Calendar.MONTH,
                Calendar.LONG,
                en)
        } ${cal[Calendar.YEAR]}"

        //Dengan ViewBinding
//        bindings.txtAyahCounter.text = "Ayat 1"
        //Bisa juga dijadikan variable

        val namasurat = bindings.txtSurahName
        var isClickedFromHome = false
        namasurat.text = LastReadPreferences(context).lastReadSurah

        bindings.txtSalam.text = tanggalanHijriyyah
        bindings.txtAyahCounter.text = LastReadPreferences(context).lastReadAyah

        val adapter = ViewPageAdapter(this, fragmentList)
        val surahNameLasRead = LastReadPreferences(context).lastReadSurah

        bindings.lastReadTeleport.setOnClickListener {
            if (LastReadPreferences(context).lastReadSurah == "Belum Baca") {
                Toast.makeText(context,
                    "Anda Belum Membaca apa-apa, silahkan baca terlebih dahulu",
                    Toast.LENGTH_SHORT).show()
            } else {
                isClickedFromHome = true
                val bundle = bundleOf(FragmentQuranRead.HOMEFALSEORTRUE to isClickedFromHome,
                    FragmentQuranRead.KEY_NAMA_SURAT_LAST to surahNameLasRead,
                    FragmentQuranRead.KEYSURAHNUMBERBOOKMARK to LastReadPreferences(context).lastReadSurahNumber)
                findNavController().navigate(R.id.action_nav_home_to_nav_read_quran, bundle)
            }
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindings.penarik.isVisible = false
        }

        val heightDp = resources.displayMetrics.run { heightPixels / density }

        BottomSheetBehavior.from(bindings.bottomSheetlayout).apply {
            peekHeight = heightDp.toInt()

//            peekHeight = 370
        }

        bindings.penarik.setOnClickListener {
            if (BottomSheetBehavior.from(bindings.bottomSheetlayout).state == BottomSheetBehavior.STATE_COLLAPSED) {
                BottomSheetBehavior.from(bindings.bottomSheetlayout).state =
                    BottomSheetBehavior.STATE_EXPANDED
            } else if (BottomSheetBehavior.from(bindings.bottomSheetlayout).state == BottomSheetBehavior.STATE_EXPANDED) {
                BottomSheetBehavior.from(bindings.bottomSheetlayout).state =
                    BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        bindings.bookmarkBtn.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_fragmentBookmark)
        }

        bindings.viewPager.adapter = adapter
        bindings.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setPositionTab(position)
            }
        })
        TabLayoutMediator(bindings.tabLayout, bindings.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
        //Struktur: variableBinding.id.function
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_surah, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.searchButton -> {
                findNavController().navigate(R.id.action_nav_home_to_searchFragmentSurah)
                return true
            }
            else -> {
                false
            }
        }
    }

    private fun dayConvertertoId(hijriDate:String?): Int {
        return when (hijriDate) {
            "Sunday" -> {
                6
            }
            "Monday" -> {
                0
            }
            "Tuesday" -> {
                1
            }
            "Wednesday" -> {
                2
            }
            "Thursday" -> {
                3
            }
            "Friday" -> {
                4
            }
            "Saturday" -> {
                5
            }
            else -> {
                7
            }
        }
    }
}