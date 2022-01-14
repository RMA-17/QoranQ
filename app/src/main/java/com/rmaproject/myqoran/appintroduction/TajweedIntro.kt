package com.rmaproject.myqoran.appintroduction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.TajweedSlideBinding
import com.rmaproject.myqoran.model.Quran
import com.rmaproject.myqoran.ui.tajwid.QuranArabicUtils

class TajweedIntro : Fragment(R.layout.tajweed_slide) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tajweed_slide, container, false)
    }

    private val binding : TajweedSlideBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = "Pengenalan dengan tajwid"
        binding.description.text = "Quran ini dilengkapi dengan tajwid yang berwarna"
        binding.image.setImageResource(R.drawable.white)

    }
}