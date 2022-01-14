package com.rmaproject.myqoran.ui.search.by.surah

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.data.QuranDatabase
import com.rmaproject.myqoran.databinding.SearchLayoutSurahBinding
import com.rmaproject.myqoran.ui.home.QuranViewModel
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranRead
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranReadAdapter

class SearchFragmentSurah : Fragment(R.layout.search_layout_surah) {

    private val binding : SearchLayoutSurahBinding by viewBinding()
    private val viewModel : QuranViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)
        showOrhideKeyboard(true)
        // listener untuk deteksi enter dari keyboard
        binding.querytextSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.querytextSearch.text.toString()
                setQuranAdapter(query)
                true
            }
            false
        }
        binding.searchReadToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
            showOrhideKeyboard(false)
        }
    }

    private fun setQuranAdapter (query:String) {
        val quranDao = QuranDatabase.getInstance(requireContext()).quranDao()
        viewModel.getTotalAyahList().observe(viewLifecycleOwner, { totalAyahList ->
            quranDao.searchSurah("%$query%").asLiveData().observe(viewLifecycleOwner, Observer { surahList ->
                val adapter = SearchSurahAdapter(surahList, totalAyahList) { surah ->
                    val isFromSearch = true
                    val surahNumber: Int = surah.surahNumber ?: 1
                    val surahName: String = surah.SurahName_en.toString()
                    val bundle = bundleOf(FragmentQuranRead.KEY_SURAH_NUMBER to surahNumber,
                        FragmentQuranRead.KEYSURAHNUMBERBOOKMARK to surah.surahNumber,
                        FragmentQuranRead.KEY_NAMA_SURAT to surahName, FragmentQuranRead.KEY_DARI_SEARCH to isFromSearch)
                    findNavController().navigate(R.id.action_searchFragmentSurah_to_nav_read_quran, bundle)
                }
                binding.recyclerViewSurahList.layoutManager = LinearLayoutManager(context)
                binding.recyclerViewSurahList.adapter = adapter
            })
        })


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
}