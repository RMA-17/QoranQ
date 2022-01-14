package com.rmaproject.myqoran.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.SettingsFragmentBinding
import android.view.ViewTreeObserver.OnScrollChangedListener
import androidx.core.widget.NestedScrollView

class FragmentSettings : Fragment(R.layout.settings_fragment) {

    private val binding: SettingsFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.btmAppBar)

        binding.scrollView.setOnScrollChangeListener { _: NestedScrollView, dx: Int, dy: Int, _: Int, _: Int ->
            if (dy > 0) {
                bottomAppBar?.performHide(true)
            } else if (dy < 0) {
                bottomAppBar?.performShow(true)
            }
        }

        binding.textPreview.textSize = FontSizePreferences(context).gantiFontSize.toFloat()

        val fontchangerSlider = binding.resizeSlider
        fontchangerSlider.value = FontSizePreferences(context).gantiFontSize.toFloat()

        if (FontSizePreferences(context).gantiFontSize == 35){
            binding.previewtext.text = "Preview (Default)"
        } else {
            binding.previewtext.text = "Preview"
        }

        fontchangerSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                binding.textPreview.textSize = slider.value
                if (slider.value == 35F){
                    binding.previewtext.text = "Preview (Default)"
                } else {
                    binding.previewtext.text = "Preview"
                }

            }

            override fun onStopTrackingTouch(slider: Slider) {
                binding.textPreview.textSize = slider.value
                FontSizePreferences(context).gantiFontSize = slider.value.toInt()
                if (slider.value == 35F){
                    binding.previewtext.text = "Preview (Default)"
                } else {
                    binding.previewtext.text = "Preview"
                }
            }
        })

        fontchangerSlider.addOnChangeListener { slider, value, fromUser ->
            binding.textPreview.textSize = value
            if (value == 35F){
                binding.previewtext.text = "Preview (Default)"
            } else {
                binding.previewtext.text = "Preview"
            }
        }


        val listQari = arrayOf("Mishari Rasyid Alafasy (Default)", "Muhammad Ayyoub", "Abdurrahmad As-Sudais", "Hudaify")
        binding.changeQari.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Pilih Qari")
                .setItems(listQari) { _, which ->
                    when (which) {
                        0 -> {
                            QariPreferences(context).qariPreference = 0
                            binding.currentQariTracker.text = "Current Qari: ${listQari[0]}"
                        }
                        1 -> {
                            QariPreferences(context).qariPreference = 1
                            binding.currentQariTracker.text = "Current Qari: ${listQari[1]}"
                        }
                        2 -> {
                            QariPreferences(context).qariPreference = 2
                            binding.currentQariTracker.text = "Current Qari: ${listQari[2]}"
                        }
                        3 -> {
                            QariPreferences(context).qariPreference = 3
                            binding.currentQariTracker.text = "Current Qari: ${listQari[3]}"
                        }
                    }
                }
                .show()
        }
        when (QariPreferences(context).qariPreference) {
            0 -> {
                binding.currentQariTracker.text = "Current Qari: ${listQari[0]}"
            }
            1 -> {
                binding.currentQariTracker.text = "Current Qari: ${listQari[1]}"
            }
            2 -> {
                binding.currentQariTracker.text = "Current Qari: ${listQari[2]}"
            }
            3 -> {
                binding.currentQariTracker.text = "Current Qari: ${listQari[3]}"
            }
        }

        binding.changeColorListener.setOnClickListener {
            findNavController().navigate(R.id.action_nav_settings_to_bottomSheetColorChanger)
        }

        when(FocusOnReadModePreferences(requireContext()).foModeOn){
            true -> {
                binding.switchReadMode.isChecked = true
            }
            false -> {
                binding.switchReadMode.isChecked = false
            }
        }

        binding.switchReadMode.setOnCheckedChangeListener { _, isChecked ->
            FocusOnReadModePreferences(requireContext()).foModeOn = isChecked
        }
    }

    companion object{
        const val DarkModeOn = 1
        const val DarkModeOff = 0
    }

//    private fun saveData() {
//        val setToDark = AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        val sharedPreferences : SharedPreferences
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//        val editor = sharedPreferences.edit()
//        editor.apply {
//            putString("KEY_SIMPAN", setToDark)
//        }
//  Settings fragment written by Raka M.A
//    }

}