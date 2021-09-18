package com.rmaproject.myqoran.ui.quran.indexby.jozz

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.data.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentQuranIndexByJozzBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.ui.quran.indexby.surah.QuranIndexAdapterSurah

class QuranIndexFragmentByJozz : Fragment(R.layout.fragment_quran_index_by_jozz) {

    private val binding:FragmentQuranIndexByJozzBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            val database = QuranDatabase.getInstance(it)
            val quranDao = database.quranDao()
            quranDao.showQuranIndexByJozz().asLiveData().observe(viewLifecycleOwner, { jozzList ->
                Log.d("CHECKDATA", jozzList.size.toString())
                val adapter = QuranIndexAdapterJozz(jozzList) { jozzList ->
                    val jozzNumber: Int = jozzList.juzNumber ?: 1
                    val totalAyat: Int = jozzList.numberOfAyah ?: 1
                    val bundle = bundleOf("JOZZNUMBER" to jozzNumber, "TOTALAYAT" to totalAyat)
                    findNavController().navigate(R.id.action_nav_home_to_nav_read_quran, bundle)
                }
                binding.RecyclerViewAyat.adapter = adapter
                binding.RecyclerViewAyat.layoutManager = LinearLayoutManager(context)
            })
        }


    }
}