package com.rmaproject.myqoran.ui.settings

import android.content.Context
import androidx.preference.PreferenceManager

class ThemePreferences (context: Context?) {

    companion object{
        private const val DARK_STATUS = "DARKMODE"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    var darkMode = preferences.getInt(DARK_STATUS, 0)
    set(value) = preferences.edit().putInt(DARK_STATUS, value).apply()
}