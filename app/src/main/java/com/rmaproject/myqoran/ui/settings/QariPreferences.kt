package com.rmaproject.myqoran.ui.settings

import android.content.Context
import androidx.preference.PreferenceManager

class QariPreferences (context: Context?){

    companion object {
     private const val KEYQORI = "QORIKEY"
    }

    private val preferencesQari = PreferenceManager.getDefaultSharedPreferences(context)
    var qariPreference = preferencesQari.getInt(KEYQORI, 0)
    set(value) = preferencesQari.edit().putInt(KEYQORI, value).apply()

}