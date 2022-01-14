package com.rmaproject.myqoran.ui.settings

import android.content.Context
import androidx.preference.PreferenceManager

class FocusOnReadModePreferences (context: Context) {
    private companion object {
        const val focsModeKey = "KEYFOCMODE"
    }

    private val focusReadPreference = PreferenceManager.getDefaultSharedPreferences(context)
    var foModeOn = focusReadPreference.getBoolean(focsModeKey, false)
    set(value) = focusReadPreference.edit().putBoolean(focsModeKey, value).apply()
}