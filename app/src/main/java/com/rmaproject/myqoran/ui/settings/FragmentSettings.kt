package com.rmaproject.myqoran.ui.settings

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.SettingsFragmentBinding

class FragmentSettings : Fragment(R.layout.settings_fragment) {

    private val binding: SettingsFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(ThemePreferences(context).darkMode){
            DarkModeOn -> {
                binding.switchtheme.isChecked = true
            }
            DarkModeOff -> {
                binding.switchtheme.isChecked = false
            }
        }
        
        binding.switchtheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                ThemePreferences(context).darkMode = DarkModeOn
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                ThemePreferences(context).darkMode = DarkModeOff
            }
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
//
//    }

}