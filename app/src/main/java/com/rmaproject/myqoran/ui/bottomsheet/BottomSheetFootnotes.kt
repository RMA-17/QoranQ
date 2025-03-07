package com.rmaproject.myqoran.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
/*
            Code writen by Raka M.A
             */
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.BottomSheetFootnotesBinding

class BottomSheetFootnotes : BottomSheetDialogFragment() {
    val binding : BottomSheetFootnotesBinding by viewBinding(R.id.buttomSheet)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_footnotes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivewFootnote = arguments?.getString(KEY_TERIMA_FOOTNOTE) ?: ""
        binding.textFootnote.text = receivewFootnote.replace("\n", "\n\n")
        binding.toolbar.setOnMenuItemClickListener{
            findNavController().navigateUp()
        }

    }

    companion object {
        const val KEY_TERIMA_FOOTNOTE = "TERIMAFOOTNOTE"
    }

}
/*
            Code writen by Raka M.A
             */