package com.rmaproject.myqoran.ui.splash_screen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.rmaproject.myqoran.MainActivity
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.appintroduction.AppIntroActivity
import com.rmaproject.myqoran.ui.settings.AppIntroPreferences
import com.rmaproject.myqoran.ui.settings.ThemePreferences

class SplashScreenActivity : AppCompatActivity(R.layout.splash_screen) {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        when (AppIntroPreferences(this).isAppIntroducted) {
            true -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            false -> {
                val intent = Intent(this, AppIntroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        when (ThemePreferences(this).darkMode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }

}