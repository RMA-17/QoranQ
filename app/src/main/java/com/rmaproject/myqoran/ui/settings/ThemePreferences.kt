package com.rmaproject.myqoran.ui.settings

import android.content.Context
/*This code was Written by Raka M.A*/
import androidx.preference.PreferenceManager

class ThemePreferences (context: Context?) {

    companion object{
        //Untuk memulai sharedpreferences
        private const val DARK_STATUS = "DARKMODE"
        private const val ACCENTCHANGER = "GANTITEMA"
    }

    private val preferencesColor = PreferenceManager.getDefaultSharedPreferences(context)
    var darkMode = preferencesColor.getInt(DARK_STATUS, 0)
    set(value) = preferencesColor.edit().putInt(DARK_STATUS, value).apply()
    var changeAccent = preferencesColor.getInt(ACCENTCHANGER, 0)
    set(value) = preferencesColor.edit().putInt(ACCENTCHANGER, value).apply()
}