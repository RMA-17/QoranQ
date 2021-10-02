package com.rmaproject.myqoran.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ButtomsheetlicenseBinding

class ButtomSheetLicense : BottomSheetDialogFragment() {

    val binding : ButtomsheetlicenseBinding by viewBinding(R.id.buttomSheetlicense)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.buttomsheetlicense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveLicense = arguments?.getString("KIRIMLISENSI") ?: "TIdak terkirim"
        binding.textFootnote.text = receiveLicense
        binding.toolbar.setOnMenuItemClickListener{
            findNavController().navigateUp()
        }
    }
}