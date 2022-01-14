package com.rmaproject.myqoran.ui.settings

import android.content.Context
import androidx.preference.PreferenceManager

class PrayerTimePreferences (context: Context) {

    companion object {
        private const val WAKTUSHUBUH = "SHUBUH"
        private const val WAKTUZUHUR = "ZUHUR"
        private const val WAKTUASHAR = "ASHAR"
        private const val WAKTUMAGHRIB = "MAGHRIB"
        private const val WAKTUISYA = "ISYA"
    }

    private val preferenceWaktuSholat = PreferenceManager.getDefaultSharedPreferences(context)

    var waktuShubuhPreferences = preferenceWaktuSholat.getString(WAKTUSHUBUH, "Error")
    set(value) = preferenceWaktuSholat.edit().putString(WAKTUSHUBUH, value).apply()

    var waktuZuhurPreferences = preferenceWaktuSholat.getString(WAKTUZUHUR, "Error")
    set(value) = preferenceWaktuSholat.edit().putString(WAKTUZUHUR, value).apply()

    var waktuAsharPreferences = preferenceWaktuSholat.getString(WAKTUASHAR, "Error")
    set(value) = preferenceWaktuSholat.edit().putString(WAKTUASHAR, value).apply()

    var waktuMaghribPreferences = preferenceWaktuSholat.getString(WAKTUASHAR, "Error")
    set(value) = preferenceWaktuSholat.edit().putString(WAKTUMAGHRIB, value).apply()

    var waktuIsyaPreferences = preferenceWaktuSholat.getString(WAKTUISYA, "Error")
    set(value) = preferenceWaktuSholat.edit().putString(WAKTUISYA, value).apply()
}