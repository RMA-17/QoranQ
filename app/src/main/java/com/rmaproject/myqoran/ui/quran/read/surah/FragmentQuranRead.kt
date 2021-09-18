package com.rmaproject.myqoran.ui.quran.read.surah

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.data.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentReadQuranBinding

class FragmentQuranRead: Fragment(R.layout.fragment_read_quran) {

    val binding:FragmentReadQuranBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val surahNumber = arguments?.getInt("SURAHNUMBERDISPLAY") ?: 1
        val totalAyah = arguments?.getInt("TOTALAYAT") ?: 1
        val jozzNumber = arguments?.getInt("JOZZNUMBER") ?: 1
        val pageNumber = arguments?.getInt("PAGENUMBER") ?: 1


        //Untuk mengakses sesuatu yang ada di folder res, butuh context

        //Surah
        context?.let{
            val database = QuranDatabase.getInstance(it)
            val quranDao = database.quranDao()
            quranDao.readQuranBySurah(surahNumber).asLiveData().observe(viewLifecycleOwner, Observer { listQuran ->
                val adapter = FragmentQuranReadAdapter(listQuran, totalAyah)
                binding.RecyclerVIew.adapter = adapter
                binding.RecyclerVIew.layoutManager = LinearLayoutManager(context)
            })
        }
        //Jozz
        context?.let {
            val database = QuranDatabase.getInstance(it)
            val quranDao = database.quranDao()
            quranDao.readQuranByJozz(jozzNumber).asLiveData().observe(viewLifecycleOwner, Observer { listQuran ->
                val adapter = FragmentQuranReadAdapter(listQuran, totalAyah)
                binding.RecyclerVIew.adapter = adapter
                binding.RecyclerVIew.layoutManager = LinearLayoutManager(context)

            })
        }

        context?.let {
            val database = QuranDatabase.getInstance(it)
            val quranDao = database.quranDao()
            quranDao.readQUranByPage(pageNumber).asLiveData().observe(viewLifecycleOwner, Observer { listQuran ->
                val adapter = FragmentQuranReadAdapter(listQuran, totalAyah)
                binding.RecyclerVIew.adapter = adapter
                binding.RecyclerVIew.layoutManager = LinearLayoutManager(context)
            })
        }

    }
}