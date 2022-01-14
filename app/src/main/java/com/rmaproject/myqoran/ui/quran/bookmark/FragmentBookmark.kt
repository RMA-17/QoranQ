package com.rmaproject.myqoran.ui.quran.bookmark

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.data.BookmarkDatabase
import com.rmaproject.myqoran.databinding.BookmarkLayoutBinding
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranRead
import kotlinx.coroutines.launch

class FragmentBookmark : Fragment(R.layout.bookmark_layout) {
    private val binding : BookmarkLayoutBinding by viewBinding()
    private lateinit var adapter:BookmarkAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        val database = BookmarkDatabase.getInstance(requireContext())
        val quranDao = database.quranDao()
        var isClickedFromHere: Boolean

        quranDao.getBookmarks().asLiveData().observe(viewLifecycleOwner, { listBookmark ->
            adapter = BookmarkAdapter(listBookmark) { bookmark ->
                isClickedFromHere = true
                val bundle = bundleOf(FragmentQuranRead.KEY_DARI_BOOKMARK to isClickedFromHere, FragmentQuranRead.KEYSURAHNUMBERBOOKMARK to bookmark.surahNumber,
                FragmentQuranRead.KEYSURAHNAMEBOOKMARK to bookmark.surahName, FragmentQuranRead.KEY_POSSCROLL to bookmark.positionScroll)
                findNavController().navigate(R.id.action_fragmentBookmark_to_nav_read_quran, bundle)
            }
            if (adapter.itemCount >= 1){
                binding.peringatan.isVisible = false
            } else {
                binding.peringatan.isVisible
            }
            binding.bookmarkRV.adapter = adapter
            binding.bookmarkRV.layoutManager = LinearLayoutManager(context)
            adapter.hapusListonClick = { bookmark ->
                lifecycleScope.launch {
                    quranDao.deleteBookmark(bookmark)
                }
            }
            val toolbarActivity = requireActivity().findViewById<Toolbar>(R.id.toolbar)
            toolbarActivity.title = "Bookmarks"
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_all, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.deleteButton -> {
                if (adapter.itemCount >= 1) {
                    val database = BookmarkDatabase.getInstance(requireContext())
                    val quranDao = database.quranDao()
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Konfirmasi")
                        .setMessage("Yakin ingin menghapus seluruh isi bookmark anda?")
                        .setNegativeButton("Tidak") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Ya") { dialog, which ->
                            lifecycleScope.launch{
                                quranDao.deleteAllBookmark()
                            }
                            dialog.dismiss()
                        }
                        .show()
                    true
                } else {
                    Toast.makeText(requireContext(), "Anda belum menambahkan bookmark apa-apa, silahkan tambahkan", Toast.LENGTH_SHORT).show()
                    false
                }
            }
            else -> {
                false
            }
        }

    }
}