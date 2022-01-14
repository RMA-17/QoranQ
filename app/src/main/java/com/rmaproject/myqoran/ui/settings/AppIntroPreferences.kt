package com.rmaproject.myqoran.ui.settings

import android.content.Context
import androidx.preference.PreferenceManager

class AppIntroPreferences (context: Context) {

    private companion object {
        const val KEY = "THISISKEY"
    }

    private val showIntro = PreferenceManager.getDefaultSharedPreferences(context)
    var isAppIntroducted = showIntro.getBoolean(KEY, false)
    set(value) = showIntro.edit().putBoolean(KEY, value).apply()
}