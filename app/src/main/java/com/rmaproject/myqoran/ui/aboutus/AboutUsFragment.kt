package com.rmaproject.myqoran.ui.aboutus

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentHomeBinding

class AboutUsFragment : Fragment(R.layout.fragment_about) {


    private val bindings:FragmentHomeBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }
}