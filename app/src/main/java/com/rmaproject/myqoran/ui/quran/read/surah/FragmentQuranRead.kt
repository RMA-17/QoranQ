package com.rmaproject.myqoran.ui.quran.read.surah

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.control.RepeatMode
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.data.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentReadQuranBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.ui.bottomsheet.ButtomSheet
import com.rmaproject.myqoran.ui.home.QuranViewModel

class FragmentQuranRead: Fragment(R.layout.fragment_read_quran) {

    val binding:FragmentReadQuranBinding by viewBinding()
    private val viewModel:QuranViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val surahNumber = arguments?.getInt(KEY_SURAH_NUMBER) ?: 1
        val jozzNumber = arguments?.getInt(KEY_JOZZ_NUMBER) ?: 1
        val pageNumber = arguments?.getInt(KEY_PAGE_NUMBER) ?: 1
        val surahName = arguments?.getString(KEY_NAMA_SURAT) ?: ""
        var toolbarTitle:String = ""

        val database = QuranDatabase.getInstance(requireContext())
        val quranDao = database.quranDao()

        viewModel.getPositionTab().observe(viewLifecycleOwner, { tabPos ->
            viewModel.getTotalAyahList().observe(viewLifecycleOwner, { totalAyah ->
                when (tabPos){
                    TAB_SURAH -> {
                        quranDao.readQuranBySurah(surahNumber).asLiveData().observe(viewLifecycleOwner, Observer { listQuran ->
                            setQuranAdapter(listQuran, totalAyah)
                        })
                        toolbarTitle = surahName
                    }
                    TAB_JUZ -> {
                        quranDao.readQuranByJozz(jozzNumber).asLiveData().observe(viewLifecycleOwner, Observer { listQuran ->
                            setQuranAdapter(listQuran, totalAyah)
                        })
                        toolbarTitle = "Juz $jozzNumber"
                    }
                    TAB_PAGE -> {
                        quranDao.readQUranByPage(pageNumber).asLiveData().observe(viewLifecycleOwner, Observer { listQuran ->
                            setQuranAdapter(listQuran, totalAyah)
                        })
                        toolbarTitle = "page $pageNumber"
                    }
                }
                val toolbarActivity = requireActivity().findViewById<Toolbar>(R.id.toolbar)
                toolbarActivity.title = toolbarTitle

            })

        })
    }

    private fun setQuranAdapter(listQuran:List<Quran>, totalAyah: List<Int>){
        val adapter = FragmentQuranReadAdapter(listQuran, totalAyah)
        adapter.copyOnclickListener = { quran, position ->
            val text:String = "${quran.TextQuran}" +
                    "\n" +
                    "\n" +
                    "${quran.translation}" +
                    "\n" +
                    "\n" +
                    "QS. Surah ${quran.SurahName_en}: ${quran.AyahNumber}"
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copy ayah to Clipboard", text)
            clipboard.setPrimaryClip(clip)
            //Untuk mengakses sesuatu yang ada di folder res, butuh context
            //Jika dalam activity, penulisannya begini: val share:Intent = ShareCompat.IntentBuilder(Context)
            //Jika dalam fragment seperti ini:
            Toast.makeText(requireContext(), "Ayat berhasil disalin", Toast.LENGTH_SHORT).show()
        }
        adapter.shareOnclickListener = { quran, position ->
            val text:String = "${quran.TextQuran}" +
                    "\n" +
                    "\n" +
                    "${quran.translation}" +
                    "\n" +
                    "\n" +
                    "QS. Surah ${quran.SurahName_en}: ${quran.AyahNumber}"
            //ShareCompat digunakan untuk meng-share content
            //Jika dalam activity, penulisannya begini: val share:Intent = ShareCompat.IntentBuilder(Context)
            //Jika dalam fragment seperti ini:
            ShareCompat.IntentBuilder(requireContext())
                .setText(text)
                .setType("text/plain")
                .setChooserTitle("Judul")
                .startChooser()

        }
        binding.RecyclerVIew.adapter = adapter
        binding.RecyclerVIew.layoutManager = LinearLayoutManager(context)
        adapter.footNoteOnClickListener = { quran ->
            val bundle = bundleOf(ButtomSheet.KEY_TERIMA_FOOTNOTE to quran.footnotes)
            findNavController().navigate(R.id.action_nav_read_quran_to_buttomSheet, bundle)

        }
        adapter.playMurottalListener = { quran, position->
            val numberofSurah = String.format("%03d", quran.surahNumber)
            val ayatNumber = String.format("%03d",quran.AyahNumber)
            val url = "https://archive.org/download/quran-every-ayah/Mishary%20Rashid%20Alafasy.zip/$numberofSurah$ayatNumber.mp3"
            val audio = SongInfo()
            audio.songId = "$numberofSurah$ayatNumber"
            audio.songUrl = url
            audio.artist = "Mishary Rashid Alafasy"
            audio.songName = "${quran.SurahName_en} : ${quran.AyahNumber}"
            audio.songCover = "https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/10/04/676590316.jpg"
            StarrySky.with().playMusicByInfo(audio)
            /*
            Code writen by Raka M.A
             */
            StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_NONE, isLoop = false)
            Toast.makeText(requireContext(), "Murottal Played", Toast.LENGTH_SHORT).show()

        }

    }

    //Companion object berguna untuk menyetor sebuah const value, dan bisa digunakan di tempat lain
    companion object {
        const val KEY_SURAH_NUMBER = "SURAHNUMBERDISPLAY"
        const val KEY_JOZZ_NUMBER = "JOZZNUMBER"
        const val KEY_PAGE_NUMBER = "PAGENUMBER"
        const val TAB_SURAH = 0
        const val TAB_JUZ = 1
        const val TAB_PAGE = 2
        const val KEY_NAMA_SURAT = "NAMASURAT"
    }
}