package com.rmaproject.myqoran.ui.quran.indexby.surah

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.data.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentQuranIndexBySurahBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.model.Surah
import com.rmaproject.myqoran.ui.quran.indexby.page.QuranIndexAdapterPage

class QuranIndexFragmentBySurah : Fragment(R.layout.fragment_quran_index_by_surah) {

    private val binding:FragmentQuranIndexBySurahBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) //arrayListof() berguna sebagai default value bagi sebuah array

        context?.let {
            val database = QuranDatabase.getInstance(it)
            val quranDao = database.quranDao()
            quranDao.showQuranIndexBySurah().asLiveData().observe(viewLifecycleOwner, { surahList ->
                Log.d("CHECKDATA", surahList.size.toString())
                val adapter = QuranIndexAdapterSurah(surahList) { surah -> // It = ya itu tuh. Bisa diganti
                    val surahNumber: Int = surah.surahNumber ?: 1
                    val totalAyah:Int = surah.numberOfAyah ?: 1
                    val bundle = bundleOf("SURAHNUMBERDISPLAY" to surahNumber, "TOTALAYAT" to totalAyah)
                    findNavController().navigate(R.id.action_nav_home_to_nav_read_quran, bundle)

                }
                binding.RecyclerViewAyat.adapter = adapter
                binding.RecyclerViewAyat.layoutManager = LinearLayoutManager(context)
            })
        }

    }

}

