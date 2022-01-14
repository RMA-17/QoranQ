package com.rmaproject.myqoran.ui.quran.indexby.jozz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
/*
            Code writen by Raka M.A
             */
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.data.QuranDatabase
import com.rmaproject.myqoran.databinding.FragmentQuranIndexByJozzBinding
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranRead

class QuranIndexFragmentByJozz : Fragment(R.layout.fragment_quran_index_by_jozz) {

    private val binding: FragmentQuranIndexByJozzBinding by viewBinding()

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
            quranDao.showQuranIndexByJozz().asLiveData().observe(viewLifecycleOwner, { jozzList ->
                Log.d("CHECKDATA", jozzList.size.toString())
                val adapter = QuranIndexAdapterJozz(jozzList) { jozzList ->
                    val jozzNumber: Int = jozzList.juzNumber ?: 1
                    val isFromJozz = true
                    val bundle = bundleOf(FragmentQuranRead.KEY_JOZZ_NUMBER to jozzNumber, FragmentQuranRead.KEYJOZZNUMBERFORSEARCH to jozzList.juzNumber,
                    FragmentQuranRead.KEYFROMINDEXJOZZ to isFromJozz)
                    findNavController().navigate(R.id.action_nav_home_to_nav_read_quran, bundle)
                }
                binding.RecyclerViewAyat.setLayoutManager(LinearLayoutManager(context))
                binding.RecyclerViewAyat.setAdapter(adapter)
            })
        }
    }
}