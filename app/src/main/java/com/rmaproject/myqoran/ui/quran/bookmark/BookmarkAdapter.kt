package com.rmaproject.myqoran.ui.quran.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.l4digital.fastscroll.FastScroller
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ItemBookmarkBinding
import com.rmaproject.myqoran.model.Bookmark
import com.rmaproject.myqoran.ui.quran.bookmark.BookmarkAdapter.BookmarkAdapterViewHolder

class BookmarkAdapter(
    private val bookmarkList:List<Bookmark>, private val clickListener:(Bookmark) -> Unit
) : RecyclerView.Adapter<BookmarkAdapterViewHolder>(), FastScroller.SectionIndexer {

    var hapusListonClick:((Bookmark) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
        return BookmarkAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkAdapterViewHolder, position: Int) {
        val positions = position + 1
        val bookmark = bookmarkList[position]
        holder.bindView(bookmark)
        holder.binding.clickableLayout.setOnClickListener {
            clickListener.invoke(bookmark)
        }
        holder.binding.hapuslist.setOnClickListener {
            hapusListonClick?.invoke(bookmark)
        }
        holder.binding.surahlocator.text = positions.toString()
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }
    class BookmarkAdapterViewHolder(itemview:View) : RecyclerView.ViewHolder(itemview) {
        val binding : ItemBookmarkBinding by viewBinding()
        fun bindView(bookmark:Bookmark) {
            binding.ayatTracker.text = bookmark.ayatNumber.toString()
            binding.surahTracker.text = bookmark.surahName
            binding.tanggalan.text = bookmark.timeStamp
            if (bookmark.textQuran != null) {
                binding.previewText.text = bookmark.textQuran
            } else {
                binding.previewText.text = "Preview tidak tersedia"
            }
        }

    }

    override fun getSectionText(position: Int): CharSequence {
        return bookmarkList[position].timeStamp
    }

}