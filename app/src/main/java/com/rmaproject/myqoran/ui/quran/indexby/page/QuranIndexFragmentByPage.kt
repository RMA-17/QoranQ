package com.rmaproject.myqoran.ui.quran.indexby.page

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
import com.rmaproject.myqoran.databinding.FragmentQuranIndexByPageBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.ui.quran.indexby.surah.QuranIndexAdapterSurah
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranRead

class QuranIndexFragmentByPage : Fragment(R.layout.fragment_quran_index_by_page) {

    private val binding:FragmentQuranIndexByPageBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            val database = QuranDatabase.getInstance(it)
            val quranDao = database.quranDao()
            quranDao.showQuranIndexByPage().asLiveData().observe(viewLifecycleOwner, { pageList ->
                Log.d("CHECKDATA", pageList.size.toString())
                val adapter = QuranIndexAdapterPage(pageList) { page ->
                    val pageNumber = page.page ?: 1
                    val bundle = bundleOf(FragmentQuranRead.KEY_PAGE_NUMBER to pageNumber)
                    findNavController().navigate(R.id.action_nav_home_to_nav_read_quran, bundle)
                }

                binding.RecyclerViewAyat.adapter = adapter
                binding.RecyclerViewAyat.layoutManager = LinearLayoutManager(context)
            })
        }





    }


}