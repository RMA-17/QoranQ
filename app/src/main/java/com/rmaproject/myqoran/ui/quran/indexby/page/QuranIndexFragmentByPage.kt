package com.rmaproject.myqoran.ui.quran.indexby.page

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
/*
            Code writen by Raka M.A
             */
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.data.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentQuranIndexByPageBinding
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranRead

class QuranIndexFragmentByPage : Fragment(R.layout.fragment_quran_index_by_page) {

    private val binding:FragmentQuranIndexByPageBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.btmAppBar)
        binding.RecyclerViewAyat.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    bottomAppBar?.performHide()
                } else if (dy < 0) {
                    bottomAppBar?.performShow()
                }
            }
        })
        context?.let {
            val database = QuranDatabase.getInstance(it)
            val quranDao = database.quranDao()
            quranDao.showQuranIndexByPage().asLiveData().observe(viewLifecycleOwner, { pageList ->
                quranDao.showAyahterakhir().asLiveData().observe(viewLifecycleOwner, { ayatTerakhir ->

                    Log.d("CHECKDATA", pageList.size.toString())
                    val adapter = QuranIndexAdapterPage(pageList, ayatTerakhir) { page ->
                        val pageNumber = page.page ?: 1
                        val isFromPage = true
                        val bundle = bundleOf(FragmentQuranRead.KEY_PAGE_NUMBER to pageNumber, FragmentQuranRead.KEYSPAGENUMBERFORSEARCH to page.page,
                        FragmentQuranRead.KEYFROMINDEXPAGE to isFromPage)
                        findNavController().navigate(R.id.action_nav_home_to_nav_read_quran, bundle)
                    }

                    binding.RecyclerViewAyat.setAdapter(adapter)
                    binding.RecyclerViewAyat.setLayoutManager(LinearLayoutManager(context))

                })
            })
        }





    }


}