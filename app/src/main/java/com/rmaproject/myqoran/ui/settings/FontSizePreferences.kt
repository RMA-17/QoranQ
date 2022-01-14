package com.rmaproject.myqoran.ui.settings

import android.content.Context
import androidx.preference.PreferenceManager

class FontSizePreferences (context : Context?) {
    companion object{
        private const val KEYRESIZE = "RESIZECOK"
    }

    private val changeFontSize = PreferenceManager.getDefaultSharedPreferences(context)
    var gantiFontSize = changeFontSize.getInt(KEYRESIZE, 35)
    set(value) = changeFontSize.edit().putInt(KEYRESIZE, value).apply()
}