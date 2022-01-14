package com.rmaproject.myqoran.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.ColorchangeSettingsBinding
import com.rmaproject.myqoran.ui.settings.FragmentSettings
import com.rmaproject.myqoran.ui.settings.ThemePreferences

class BottomSheetColorChanger : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.colorchange_settings, container, false)
    }
    private val binding:ColorchangeSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(ThemePreferences(context).changeAccent){
            DefaultAccent ->{
                binding.radioDefaultColor.isChecked = true
            }
            BlueAccent -> {
                binding.radioBlueColor.isChecked = true
            }
            GreenAccent -> {
                binding.radioGreenColor.isChecked = true
            }
            RedAccent -> {
                binding.radioRedColor.isChecked = true
            }
        }

        binding.colorRadioGroup.setOnCheckedChangeListener { group, id ->
            when(id){
                R.id.radioDefaultColor ->{
                    ThemePreferences(context).changeAccent = 0
                    Toast.makeText(context, "Tema diganti ke Default", Toast.LENGTH_LONG).show()
                    dialog?.dismiss()
                    requireActivity().recreate()
                }

                R.id.radioBlueColor -> {
                    ThemePreferences(context).changeAccent = 1
                    Toast.makeText(context, "Tema diganti ke Blue", Toast.LENGTH_LONG).show()
                    dialog?.dismiss()
                    requireActivity().recreate()
                }
                R.id.radioGreenColor -> {
                    ThemePreferences(context).changeAccent = 2
                    Toast.makeText(context, "Tema diganti ke Green", Toast.LENGTH_LONG).show()
                    dialog?.dismiss()
                    requireActivity().recreate()
                }
                R.id.radioRedColor -> {
                    ThemePreferences(context).changeAccent = 3
                    Toast.makeText(context, "Tema diganti ke Red", Toast.LENGTH_LONG).show()
                    dialog?.dismiss()
                    requireActivity().recreate()
                }
            }
        }
    }

    companion object {
        const val DefaultAccent = 0
        const val BlueAccent = 1
        const val GreenAccent = 2
        const val RedAccent = 3
    }
}