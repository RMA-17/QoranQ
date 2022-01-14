package com.rmaproject.myqoran.ui.quran.read.surah

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.control.RepeatMode
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.data.BookmarkDatabase
import com.rmaproject.myqoran.data.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentReadQuranBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.ui.bottomsheet.BottomSheetFootnotes
import com.rmaproject.myqoran.ui.home.QuranViewModel
import com.rmaproject.myqoran.ui.search.by.ayah.SearchFragmentRead
import com.rmaproject.myqoran.ui.settings.LastReadPreferences
import com.rmaproject.myqoran.ui.settings.QariPreferences
import kotlinx.coroutines.launch

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.lzx.starrysky.manager.PlaybackStage

class FragmentQuranRead: Fragment(R.layout.fragment_read_quran) {

    val binding: FragmentReadQuranBinding by viewBinding()

    private val menuRepeat by lazy {
        binding.bottomAppbar.menu.findItem(R.id.repeatAyahButton)
    }
    private val menuPlay by lazy {
        binding.bottomAppbar.menu.findItem(R.id.pauseplayAyahButton)
    }
    private val prevButton by lazy {
        binding.bottomAppbar.menu.findItem(R.id.previousAyahBtn)
    }
    private val nextButton by lazy {
        binding.bottomAppbar.menu.findItem(R.id.nextAyahButton)
    }

    private val viewModel: QuranViewModel by activityViewModels()
    //Untuk menambahkan menu kepada fragment
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_ayah, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //Untuk meng-code icon fragment tersebut.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val surahNumber = arguments?.getInt(KEYSURAHNUMBERFORSEARCH) ?: 1
        val jozzNumber = arguments?.getInt(KEYJOZZNUMBERFORSEARCH) ?: 1
        val pageNumber = arguments?.getInt(KEYSPAGENUMBERFORSEARCH) ?: 1
        val surahNumberBookmark = arguments?.getInt(KEYSURAHNUMBERBOOKMARK) ?: 1
        val isclickedFromBookmark = arguments?.getBoolean(KEY_DARI_BOOKMARK) ?: false
        val isClickedFromSearch = arguments?.getBoolean(KEY_DARI_SEARCH) ?: false

        return when (item.itemId) {
            R.id.searchbar -> {
                val isFromIndexSurah = arguments?.getBoolean(KEYFROMINDEXSURAH) ?: false
                val isFromIndexPage = arguments?.getBoolean(KEYFROMINDEXPAGE) ?: false
                val isFromIndexJozz = arguments?.getBoolean(KEYFROMINDEXJOZZ) ?: false
                val bundle = bundleOf(
                    SearchFragmentRead.KEYNOMORSURAH to surahNumber,
                    SearchFragmentRead.SEARCHSURAHKEYBOOL to isFromIndexSurah,
                    SearchFragmentRead.KEYNOMORJOZZ to jozzNumber,
                    SearchFragmentRead.SEARCHJOZZKEYBOOL to isFromIndexJozz,
                    SearchFragmentRead.KEYNOMORPAGE to pageNumber,
                    SearchFragmentRead.SEARCHPAGEKEYBOOL to isFromIndexPage,
                    SearchFragmentRead.KEYNOMORSURAH to surahNumberBookmark,
                    SearchFragmentRead.KEYDARI_SEARCH to isClickedFromSearch,
                    SearchFragmentRead.KEYDARIBOOKMARK to isclickedFromBookmark
                )
                findNavController().navigate(R.id.action_nav_read_quran_to_searchFragment, bundle)
                return true
            }
            else -> {
                false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val surahNumber = arguments?.getInt(KEY_SURAH_NUMBER) ?: 1
        val jozzNumber = arguments?.getInt(KEY_JOZZ_NUMBER) ?: 1
        val pageNumber = arguments?.getInt(KEY_PAGE_NUMBER) ?: 1
        val surahName = arguments?.getString(KEY_NAMA_SURAT) ?: ""
        var toolbarTitle = ""
        val database = QuranDatabase.getInstance(requireContext())
        val quranDao = database.quranDao()
        val isClickedFromHome = arguments?.getBoolean(HOMEFALSEORTRUE, false)
        val lastReadPos = LastReadPreferences(context).lastReadPosition
        val isclickedFromBookmark = arguments?.getBoolean(KEY_DARI_BOOKMARK) ?: false
        val lastReadSurahNumber = LastReadPreferences(context).lastReadSurahNumber
        val surahNameLastRead = arguments?.getString(KEY_NAMA_SURAT_LAST) ?: ""
        val isClickedFromSearch = arguments?.getBoolean(KEY_DARI_SEARCH) ?: false
        val lastReadPosBookmark = arguments?.getInt(KEY_POSSCROLL) ?: 0
        val surahNumberBookmark = arguments?.getInt(KEYSURAHNUMBERBOOKMARK) ?: 1
        val surahNameBookmark = arguments?.getString(KEYSURAHNAMEBOOKMARK) ?: ""
        binding.bottomAppbar.visibility = View.GONE
        binding.fabClose.visibility = View.GONE

        viewModel.getTotalAyahList().observe(viewLifecycleOwner, { totalAyah ->
            viewModel.getPositionTab().observe(viewLifecycleOwner, { tabPos ->
                Log.d("FALSEORTRUE", isClickedFromHome.toString())
                Log.d("LASTREADCHECK", lastReadPos.toString())
                when (isClickedFromSearch) {
                    true -> {
                        quranDao.readQuranBySurah(surahNumberBookmark).asLiveData()
                            .observe(viewLifecycleOwner, { listQuran ->
                                setQuranAdapter(listQuran, totalAyah, viewModel)
                            })
                        toolbarTitle = "Surah $surahName"
                    }
                    false -> {
                        when (isclickedFromBookmark) {
                            true -> {
                                quranDao.readQuranBySurah(surahNumberBookmark).asLiveData()
                                    .observe(viewLifecycleOwner, { listQuran ->
                                        setQuranAdapter(listQuran, totalAyah, viewModel)
                                    })
                                toolbarTitle = surahNameBookmark
                                if (lastReadPosBookmark <= 23) {
                                    Handler().postDelayed({
                                        binding.recyclerViewRead.smoothScrollToPosition(
                                            lastReadPosBookmark)
                                    },
                                        200)
                                } else {
                                    Handler().postDelayed(Runnable {
                                        binding.recyclerViewRead.scrollToPosition(lastReadPosBookmark)
                                    },
                                        200)
                                }
                            }
                            false -> {
                                when (isClickedFromHome) {
                                    true -> {
                                        quranDao.readQuranBySurah(lastReadSurahNumber).asLiveData()
                                            .observe(viewLifecycleOwner, { listQuran ->
                                                setQuranAdapter(listQuran, totalAyah, viewModel)
                                            })
                                        toolbarTitle = surahNameLastRead
                                        binding.recyclerViewRead.layoutManager =
                                            LinearLayoutManager(requireContext())
                                        if (lastReadPos <= 23) {
                                            Handler().postDelayed( {
                                                binding.recyclerViewRead.smoothScrollToPosition(
                                                    lastReadPos)
                                            },
                                                200)
                                        } else {
                                            Handler().postDelayed( {
                                                binding.recyclerViewRead.scrollToPosition(lastReadPos)
                                            },
                                                200)
                                        }
                                    }
                                    false -> {
                                        when (tabPos) {
                                            TAB_SURAH -> {
                                                Log.d("SURAHNUMBER", surahNumber.toString())
                                                quranDao.readQuranBySurah(surahNumber).asLiveData()
                                                    .observe(viewLifecycleOwner,
                                                         { listQuran ->
                                                            setQuranAdapter(listQuran, totalAyah, viewModel)
                                                        })
                                                toolbarTitle = "Surah $surahName"
                                            }
                                            TAB_JUZ -> {
                                                quranDao.readQuranByJozz(jozzNumber).asLiveData()
                                                    .observe(viewLifecycleOwner,
                                                         { listQuran ->
                                                            setQuranAdapter(listQuran, totalAyah, viewModel)
                                                        })
                                                toolbarTitle = "Juz $jozzNumber"
                                            }
                                            TAB_PAGE -> {
                                                quranDao.readQUranByPage(pageNumber).asLiveData()
                                                    .observe(viewLifecycleOwner,
                                                         { listQuran ->
                                                            setQuranAdapter(listQuran, totalAyah, viewModel)
                                                        })
                                                toolbarTitle = "Page $pageNumber"
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

                val toolbarActivity = requireActivity().findViewById<Toolbar>(R.id.toolbar)
                toolbarActivity.title = toolbarTitle

            })
        })

        StarrySky.with().playbackState().observe(viewLifecycleOwner, { playBackState ->
            when (playBackState.stage) {
                PlaybackStage.ERROR -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Terjadi Kesalahan")
                        .setMessage("Message: ${playBackState.errorMsg}")
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                            if (playBackState.errorMsg == "ExoPlayer error Unable to connect") {
                                StarrySky.with().stopMusic()
                                Toast.makeText(requireContext(), "Periksa Kembali internet anda", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .show()
                }
                PlaybackStage.PLAYING -> {
                    menuPlay.title = "Pause Ayah"
                    menuPlay.setIcon(R.drawable.ic_baseline_pause_24)
                }
                PlaybackStage.SWITCH -> { //Bakal kepanggil saat switch audio
                    playBackState.songInfo?.let {
                        binding.recyclerViewRead.smoothScrollToPosition(it.objectValue as Int + 1)
                    }
                }
                PlaybackStage.BUFFERING -> {
                    menuPlay.setIcon(R.drawable.ic_outline_play_arrow_24)
                    menuPlay.title = "Play Ayah"
                }
                PlaybackStage.PAUSE, PlaybackStage.IDLE -> {
                    menuPlay.setIcon(R.drawable.ic_outline_play_arrow_24)
                    menuPlay.title = "Play Ayah"
                }
            }
        })

    }

    private fun setQuranAdapter(listQuran: List<Quran>, totalAyah: List<Int>, viewModel:QuranViewModel) {
        val adapter = FragmentQuranReadAdapter(listQuran, totalAyah, viewModel)

        adapter.copyOnclickListener = { quran, _ ->
            val text: String = "${quran.TextQuran}" +
                    "\n" +
                    "\n" +
                    "${quran.translation}" +
                    "\n" +
                    "\n" +
                    "QS. Surah ${quran.SurahName_en}: ${quran.AyahNumber}"
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copy ayah to Clipboard", text)
            clipboard.setPrimaryClip(clip)
            //Untuk mengakses sesuatu yang ada di folder res, butuh context
            //Jika dalam activity, penulisannya begini: val share:Intent = ShareCompat.IntentBuilder(Context)
            //Jika dalam fragment seperti ini:
            Toast.makeText(requireContext(), "Ayat berhasil disalin", Toast.LENGTH_SHORT).show()
        }
        adapter.shareOnclickListener = { quran, _ ->
            val text: String = "${quran.TextQuran}" +
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

        binding.recyclerViewRead.adapter = adapter
        binding.recyclerViewRead.layoutManager = LinearLayoutManager(context)

        var urlNamaQari = ""
        var artist = ""
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

        adapter.footNoteOnClickListener = { quran ->
            val bundle = bundleOf(BottomSheetFootnotes.KEY_TERIMA_FOOTNOTE to quran.footnotes)
            findNavController().navigate(R.id.action_nav_read_quran_to_buttomSheet, bundle)

        }

        adapter.playMurottalListener = { quran, totalAyat ->
            if (isOnline(requireContext())) {
                StarrySky.with().stopMusic()
                //Value Default
                val numberofSurah = String.format("%03d", quran.surahNumber)
                var ayahNumber = quran.AyahNumber
                var ayahNumberUrlReady = String.format("%03d", ayahNumber)
                var url = "https://archive.org/download/quran-every-ayah/$urlNamaQari.zip/$numberofSurah$ayahNumberUrlReady.mp3"
                var songName = "${quran.SurahName_en} : $ayahNumber"
                var repeatMode = RepeatMode.REPEAT_MODE_NONE
                var isRepeated = false

                playMurrotal(artist, quran.surahNumber, ayahNumberUrlReady, url, songName, repeatMode, isRepeated)
                binding.bottomAppbar.isVisible = true
                binding.fabClose.isVisible = true

                binding.fabClose.setOnClickListener {
                    StarrySky.with().stopMusic()
                    binding.bottomAppbar.isVisible = false
                    binding.fabClose.isVisible = false
                }

                prevButton.isVisible = true
                nextButton.isVisible = true
                menuRepeat.isVisible = true

                binding.bottomAppbar.setOnMenuItemClickListener { menuitem ->
                    when (menuitem.itemId) {
                        R.id.stopAyahButton -> {
                            StarrySky.with().stopMusic()
                        }
                        R.id.nextAyahButton -> {
                            ayahNumber = ayahNumber!! + 1
                            if (StarrySky.with().isPlaying()) {
                                StarrySky.with().stopMusic()
                            }
                            if (ayahNumber!! >= totalAyat) {
                                Toast.makeText(requireContext(), "Tidak ada ayat ${totalAyat+1}", Toast.LENGTH_SHORT).show()
                                ayahNumber = totalAyat-1
                            } else {
                                ayahNumberUrlReady = String.format("%03d", ayahNumber)
                                songName = "${quran.SurahName_en} : $ayahNumber"
                                url = "https://archive.org/download/quran-every-ayah/$urlNamaQari.zip/$numberofSurah$ayahNumberUrlReady.mp3"
                                playMurrotal(artist, quran.surahNumber, ayahNumberUrlReady, url, songName, repeatMode, isRepeated)
                            }
                        }
                        R.id.previousAyahBtn -> {
                            ayahNumber = ayahNumber!! - 1
                            if (StarrySky.with().isPlaying()) {
                                StarrySky.with().stopMusic()
                            }
                            if (ayahNumber!! <= 0) {
                                Toast.makeText(requireContext(), "Tidak ada ayat 0", Toast.LENGTH_SHORT).show()
                                ayahNumber = ayahNumber!! + 1
                            } else {
                                ayahNumberUrlReady = String.format("%03d", ayahNumber)
                                songName = "${quran.SurahName_en} : $ayahNumber"
                                url = "https://archive.org/download/quran-every-ayah/$urlNamaQari.zip/$numberofSurah$ayahNumberUrlReady.mp3"
                                playMurrotal(artist, quran.surahNumber, ayahNumberUrlReady, url, songName, repeatMode, isRepeated)
                            }
                        }

                        R.id.pauseplayAyahButton -> {
                            if (menuPlay.title == "Pause Ayah") {
                                StarrySky.with().pauseMusic()
                            } else if (menuPlay.title == "Play Ayah") {
                                StarrySky.with().restoreMusic()
                            }
                        }

                        R.id.repeatAyahButton -> {
                            if (menuRepeat.title == "Repeat") {
                                isRepeated = true
                                repeatMode = RepeatMode.REPEAT_MODE_ONE
                                menuRepeat.title = "RepeatOne"
                                menuRepeat.setIcon(R.drawable.ic_outline_repeat_one_24)
                                playMurrotal(artist, quran.surahNumber, ayahNumberUrlReady, url, songName, repeatMode, isRepeated)
                            } else if (menuRepeat.title == "RepeatOne") {
                                isRepeated = false
                                repeatMode = RepeatMode.REPEAT_MODE_NONE
                                menuRepeat.title = "Repeat"
                                menuRepeat.setIcon(R.drawable.ic_outline_repeat_24)
                                playMurrotal(artist, quran.surahNumber, ayahNumberUrlReady, url, songName, repeatMode, isRepeated)
                            }
                        }
                    }
                    true
                }
                binding.recyclerViewRead.addOnScrollListener(object : OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            binding.fabClose.hide()
                        } else if (dy < 0) {
                            if (StarrySky.with().isIdle()) {
                                binding.fabClose.visibility = View.GONE
                            } else {
                                binding.fabClose.show()
                            }
                        }
                    }
                })
            } else {
                Snackbar.make(requireView(), "Play Ayah membutuhkan Koneksi internet, silahkan coba lagi", Snackbar.LENGTH_SHORT)
                    .setTextColor(MaterialColors.getColor(requireView(), R.attr.colorPrimary))
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setAction("Ok") {
                        // Kalau di kotlin ini tidak usah di isi untuk dismiss saja.
                    }
                    .setActionTextColor(MaterialColors.getColor(requireView(), R.attr.colorPrimary))
                    .setBackgroundTint(MaterialColors.getColor(requireView(), R.attr.primary_50))
                    .show()
            }
        }

        adapter.playAllAyahClickListener = { quran, totalAyat ->
            if (isOnline(requireContext())) {
                val audioList = mutableListOf<SongInfo>()
                for (i in 1..totalAyat) {
                    val audio = SongInfo()
                    val numberofSurah = String.format("%03d", quran.surahNumber)
                    val ayatNumber = String.format("%03d", i)
                    val url =
                        "https://archive.org/download/quran-every-ayah/$urlNamaQari.zip/$numberofSurah$ayatNumber.mp3"
                    audio.artist = artist
                    audio.songId = "$numberofSurah$ayatNumber"
                    audio.songCover = "https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/10/04/676590316.jpg"
                    audio.objectValue = i -1
                    audio.songUrl = url
                    audio.songName = quran.SurahName_en.toString()
                    audioList.add(audio)
                    Log.d("AYAT", totalAyat.toString())
                }
                val startAudio = quran.AyahNumber!! - 1
                StarrySky.with().setRepeatMode(RepeatMode.REPEAT_MODE_NONE, isLoop = false)
                StarrySky.with().playMusic(audioList, startAudio)
                Toast.makeText(requireContext(), "Murottal Played", Toast.LENGTH_SHORT).show()
                binding.bottomAppbar.isVisible = true
                binding.fabClose.isVisible = true

                binding.fabClose.setOnClickListener {
                    StarrySky.with().stopMusic()
                    binding.bottomAppbar.isVisible = false
                    binding.fabClose.isVisible = false
                }
                prevButton.isVisible = false
                nextButton.isVisible = false
                menuRepeat.isVisible = false

                binding.bottomAppbar.setOnMenuItemClickListener { menuitem ->

                    when (menuitem.itemId) {
                        R.id.stopAyahButton -> {
                            StarrySky.with().stopMusic()
                        }
                        R.id.pauseplayAyahButton -> {
                            if (menuPlay.title == "Pause Ayah") {
                                StarrySky.with().pauseMusic()
                            } else if (menuPlay.title == "Play Ayah") {
                                StarrySky.with().restoreMusic()
                            }
                        }
                    }
                    true
                }
                binding.recyclerViewRead.addOnScrollListener(object : OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            binding.fabClose.hide()
                        } else if (dy < 0) {
                            if (StarrySky.with().isIdle()) {
                                binding.fabClose.visibility = View.GONE
                            } else {
                                binding.fabClose.show()
                            }
                        }
                    }
                })
            } else {
                Snackbar.make(requireView(), "Play Ayah membutuhkan Koneksi internet, silahkan coba lagi", Snackbar.LENGTH_SHORT)
                    .setTextColor(MaterialColors.getColor(requireView(), R.attr.colorPrimary))
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setAction("Ok") {
                        // Kalau di kotlin ini tidak usah di isi untuk dismiss saja.
                    }
                    .setActionTextColor(MaterialColors.getColor(requireView(), R.attr.colorPrimary))
                    .setBackgroundTint(MaterialColors.getColor(requireView(), R.attr.primary_50))
                    .show()
            }
        }

        adapter.bookMarkClickListener = { bookmark ->
            lifecycleScope.launch {
                val quranDao = BookmarkDatabase.getInstance(requireContext()).quranDao()
                quranDao.insertBookmark(bookmark)

            }
        }
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            //It will check for both wifi and cellular network
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI)
        }
        return false
    }

    private fun playMurrotal(artist:String, surahNumber:Int?, FormattedayahNumber:String, url:String,
                             songName:String, repeatMode: Int, isRepeated:Boolean) {

        val audio = SongInfo()
        audio.artist = artist
        audio.songId = "$surahNumber$FormattedayahNumber"
        audio.songUrl = url
        audio.songName = songName
        audio.songCover = "https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/10/04/676590316.jpg"
        Toast.makeText(requireContext(), "Murottal Played", Toast.LENGTH_SHORT).show()

        if (!isRepeated) {
            StarrySky.with().setRepeatMode(repeatMode, isLoop = false)
            StarrySky.with().playMusicByInfo(audio)
        } else {
            StarrySky.with().setRepeatMode(repeatMode, isLoop = true)
            StarrySky.with().playMusicByInfo(audio)
        }

        if (StarrySky.with().isPaused()) {
            StarrySky.with().restoreMusic()
        }

    }


    //Companion object berguna untuk menyetor sebuah const value, dan bisa digunakan di tempat lain
    companion object {
        const val HOMEFALSEORTRUE = "KEYHOMEFALSEORTRUE"
        const val KEY_SURAH_NUMBER = "SURAHNUMBERDISPLAY"
        const val KEYSURAHNUMBERBOOKMARK = "SURAHNUMBERBOOKMARK"
        const val KEY_JOZZ_NUMBER = "JOZZNUMBER"
        const val KEY_PAGE_NUMBER = "PAGENUMBER"
        const val TAB_SURAH = 0
        const val TAB_JUZ = 1
        const val TAB_PAGE = 2
        const val KEY_NAMA_SURAT = "NAMASURAT"
        const val KEY_NAMA_SURAT_LAST = "NAMASURAHLAST"
        const val KEY_DARI_SEARCH = "DARISEARCHKEY"
        const val KEY_DARI_BOOKMARK = "DARIBOOKMARK"
        const val KEY_POSSCROLL = "LASTREAD"
        const val KEYFROMINDEXSURAH = "FROMINDEXSURAHKEY"
        const val KEYFROMINDEXPAGE = "FROMINDEXPAGE"
        const val KEYFROMINDEXJOZZ = "FROMINDEXJOZZ"
        const val KEYSURAHNAMEBOOKMARK = "SURAHNAMEBOOKMARK"
        const val KEYSURAHNUMBERFORSEARCH = "SURAHNUMBERFORSEARCH"
        const val KEYJOZZNUMBERFORSEARCH = "SURAHNUMBERFORJOZ"
        const val KEYSPAGENUMBERFORSEARCH = "PAGENUMBERFORSEARCH"
    }
}
/*
            Code writen by Raka M.A
             */