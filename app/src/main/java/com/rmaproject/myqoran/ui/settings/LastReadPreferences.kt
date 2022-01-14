package com.rmaproject.myqoran.ui.settings

import android.content.Context
import androidx.preference.PreferenceManager

class LastReadPreferences (context: Context?) {
    companion object{
        private const val LASTSURAHREADKEY = "SURATTERAKHIR"
        private const val AYAHTERAKHIRKEY = "AYAHTERAKHIR"
        private const val POSITIONKEY = "KEYPOS"
        private const val SURAHNUMBERKEY = "KEYSURAHNUMBER"
    }

    private val preferenceLastRead = PreferenceManager.getDefaultSharedPreferences(context)

    var lastReadSurah = preferenceLastRead.getString(LASTSURAHREADKEY, "Belum Baca")
    set(value) = preferenceLastRead.edit().putString(LASTSURAHREADKEY, value).apply()

    var lastReadAyah = preferenceLastRead.getString(AYAHTERAKHIRKEY, "")
    set(value) = preferenceLastRead.edit().putString(AYAHTERAKHIRKEY, value).apply()

    var lastReadSurahNumber = preferenceLastRead.getInt(SURAHNUMBERKEY, 1)
    set(value) = preferenceLastRead.edit().putInt(SURAHNUMBERKEY, value).apply()

    var lastReadPosition = preferenceLastRead.getInt(POSITIONKEY, 0)
    set(value) = preferenceLastRead.edit().putInt(POSITIONKEY, value).apply()

}