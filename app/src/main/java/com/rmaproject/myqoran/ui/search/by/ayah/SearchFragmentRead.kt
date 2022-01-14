package com.rmaproject.myqoran.ui.search.by.ayah

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.data.BookmarkDatabase
import com.rmaproject.myqoran.data.QuranDatabase
import com.rmaproject.myqoran.databinding.SearchLayoutReadBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.ui.bottomsheet.BottomSheetFootnotes
import com.rmaproject.myqoran.ui.home.QuranViewModel
import kotlinx.coroutines.launch

class SearchFragmentRead : Fragment(R.layout.search_layout_read) {

    private val binding : SearchLayoutReadBinding by viewBinding()
    private val viewModel : QuranViewModel by activityViewModels()
    private var switchIsChecked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)
        showOrhideKeyboard(true)
        val isFromIndexSurah = arguments?.getBoolean(SEARCHSURAHKEYBOOL)?: false
        val isFromIndexJozz = arguments?.getBoolean(SEARCHJOZZKEYBOOL)?: false
        val isFromIndexPage = arguments?.getBoolean(SEARCHPAGEKEYBOOL)?: false
        val isFromBookmark = arguments?.getBoolean(KEYDARIBOOKMARK)?: false
        val isFromSearch = arguments?.getBoolean(KEYDARI_SEARCH)?:false
        val surahNumber = arguments?.getInt(KEYNOMORSURAH)?: 1
        val jozzNumber = arguments?.getInt(KEYNOMORJOZZ)?: 1
        val pageNumber = arguments?.getInt(KEYNOMORPAGE)?:1
        Log.d("SURAHNUMBER", surahNumber.toString())
        Log.d("JOZZNUMBER", jozzNumber.toString())
        Log.d("PAGENUMBER", pageNumber.toString())
//        binding.switchEntire.isChecked = switchIsChecked

        binding.switchEntire.setOnCheckedChangeListener { _, isChecked ->
            switchIsChecked = isChecked
        }

        binding.querytextSearch.setOnEditorActionListener { _, actionId, event ->
            val quranDao = QuranDatabase.getInstance(requireContext()).quranDao()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.querytextSearch.text.toString()
                when (isFromSearch) {
                    true -> {
                        if (switchIsChecked){
                            quranDao.searchEntireQuran("%$query%").asLiveData().observe(viewLifecycleOwner, Observer {
                                setQuranAdapter(it)
                            })
                        } else {
                            quranDao.searchReadSurah("%$query%", surahNumber).asLiveData().observe(viewLifecycleOwner, Observer {
                                setQuranAdapter(it)
                            })
                        }
                    }
                    false -> {
                        when (isFromIndexSurah){
                            true -> {
                                if (switchIsChecked) {
                                    quranDao.searchEntireQuran("%$query%").asLiveData().observe(viewLifecycleOwner, Observer { listQuran ->
                                        setQuranAdapter(listQuran)
                                    })
                                    binding.querytextSearch.hint = "Search in this surah..."
                                } else {
                                    quranDao.searchReadSurah("%$query%", surahNumber).asLiveData().observe(viewLifecycleOwner, Observer {
                                        setQuranAdapter(it)
                                    })
                                }
                            }
                            false -> {
                                when (isFromIndexJozz) {
                                    true -> {
                                        if (switchIsChecked) {
                                            quranDao.searchEntireQuran("%$query%").asLiveData().observe(viewLifecycleOwner, Observer {
                                                setQuranAdapter(it)
                                            })
                                            binding.querytextSearch.hint = "Search in this Jozz..."
                                        } else {
                                            quranDao.searchReadJozz("%$query%", jozzNumber).asLiveData().observe(viewLifecycleOwner, Observer {
                                                setQuranAdapter(it)
                                            })
                                        }
                                    }
                                    false -> {
                                        when (isFromIndexPage) {
                                            true -> {
                                                if (switchIsChecked) {
                                                    quranDao.searchEntireQuran("%$query%").asLiveData().observe(viewLifecycleOwner, Observer {
                                                        setQuranAdapter(it)
                                                    })
                                                    binding.querytextSearch.hint = "Search in this Page..."
                                                } else {
                                                    quranDao.searchReadPage("%$query%", pageNumber).asLiveData().observe(viewLifecycleOwner, Observer {
                                                        setQuranAdapter(it)
                                                    })
                                                }
                                            }
                                            false -> {
                                                when (isFromBookmark){
                                                    true -> {
                                                        if (switchIsChecked) {
                                                            quranDao.searchEntireQuran("%$query%").asLiveData().observe(viewLifecycleOwner, Observer {
                                                                setQuranAdapter(it)
                                                            })
                                                            binding.querytextSearch.hint = "Search in this Surah..."
                                                        } else {
                                                            quranDao.searchSurah("%$query%").asLiveData().observe(viewLifecycleOwner, Observer {
                                                                setQuranAdapter(it)
                                                            })
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            false
        }

        // listener untuk deteksi enter dari keyboard

        binding.searchReadToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
            showOrhideKeyboard(false)
        }

    }

    private fun setQuranAdapter(quranList:List<Quran>){
        val adapter = SearchAyahAdapter(quranList)
        binding.recyclerViewSearchAyat.adapter = adapter
        binding.recyclerViewSearchAyat.layoutManager = LinearLayoutManager(requireContext())
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

        binding.recyclerViewSearchAyat.adapter = adapter
        binding.recyclerViewSearchAyat.layoutManager = LinearLayoutManager(context)

        adapter.footNoteOnClickListener = { quran ->
            val bundle = bundleOf(BottomSheetFootnotes.KEY_TERIMA_FOOTNOTE to quran.footnotes)
            findNavController().navigate(R.id.action_searchFragment_to_buttomSheet, bundle)

        }
        adapter.bookMarkClickListener = { bookmark ->
            lifecycleScope.launch{
                val quranDatabase = BookmarkDatabase.getInstance(requireContext())
                val quranDao = quranDatabase.quranDao()
                quranDao.insertBookmark(bookmark)
            }
        }
    }

    private fun showOrhideKeyboard(show: Boolean) {
        val ime : InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show) {
            binding.querytextSearch.requestFocus()
            ime.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } else {
            binding.querytextSearch.clearFocus()
            ime.hideSoftInputFromWindow(binding.querytextSearch.windowToken, 0)
        }
    }
    companion object {
        const val SEARCHSURAHKEYBOOL = "DARISURAH"
        const val SEARCHJOZZKEYBOOL = "DARIJOZZ"
        const val SEARCHPAGEKEYBOOL = "DARIPAGE"
        const val KEYNOMORSURAH = "NOMORSURAHKEY"
        const val KEYNOMORJOZZ = "NOMORJOZZKEY"
        const val KEYNOMORPAGE = "NOMORJOZZPAGE"
        const val KEYDARIBOOKMARK = "DARIBOOKMARK"
        const val KEYDARI_SEARCH = "KEYFROMSEARCH"
    }
}