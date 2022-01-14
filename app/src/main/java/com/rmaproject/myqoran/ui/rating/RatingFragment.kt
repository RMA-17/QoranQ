package com.rmaproject.myqoran.ui.rating

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.FragmentRateBinding
import android.content.Intent
import android.net.Uri


class RatingFragment : Fragment(R.layout.fragment_rate) {

    private val binding:FragmentRateBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rateUsButton.setOnClickListener {
            val url = "https://play.google.com/store/apps/details?id=com.rmaproject.myqoran"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }
}