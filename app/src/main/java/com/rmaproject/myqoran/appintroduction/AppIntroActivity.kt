package com.rmaproject.myqoran.appintroduction

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroCustomLayoutFragment
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.google.android.material.color.MaterialColors
import com.rmaproject.myqoran.MainActivity
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.ui.settings.AppIntroPreferences

class AppIntroActivity : AppIntro() {

//    private val binding:TajweedSlideBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = findViewById<View>(android.R.id.content).rootView

        //Add your slide fragment to Introduce
        addSlide(AppIntroFragment.newInstance(
            title = "Selamat Datang di Aplikasi MyQoran!",
            description = "Geser ke Kanan untuk memulai",
            titleColor = getColor(R.color.black),
            imageDrawable = R.drawable.ic_qoranlogoheader,
            descriptionColor = getColor(R.color.black),
            backgroundColor = getColor(R.color.white)
        ))

        addSlide(AppIntroFragment.newInstance(
            title = "Nyalakan Lokasi",
            description = "Lokasi akan digunakan untuk mencari jadwal sholat di tempat kamu",
            imageDrawable = R.drawable.logo_lokasi,
            titleColor = getColor(R.color.black),
            descriptionColor = getColor(R.color.black),
            backgroundColor = getColor(R.color.white)
        ))

        addSlide(AppIntroFragment.newInstance(
            title = "Pilih Tema",
            description = "Tema bisa diubah antara gelap atau terang di menu sebelah kiri",
            titleColor  = getColor(R.color.black),
            imageDrawable =  R.drawable.ic_night_svgrepo_com,
            descriptionColor = getColor(R.color.black),
            backgroundColor = getColor(R.color.white)
        ))

        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.tajweed_slide))

        addSlide(AppIntroFragment.newInstance(
            title = "Semuanya selesai!",
            descriptionColor = getColor(R.color.black),
            description = "Silahkan menggunakan aplikasi ini! Baarakallahu Fiik",
            titleColor = getColor(R.color.black),
            imageDrawable = R.drawable.ic_check_svgrepo_com,
            backgroundColor = getColor(R.color.white)
        ))

        //Transform Transition
        setTransformer(AppIntroPageTransformerType.Parallax(
            titleParallaxFactor = 1.0,
            imageParallaxFactor = 7.0,
            descriptionParallaxFactor = 2.0
        ))

        //Translate Done and Skip
        setDoneText("Selesai")
        setSkipText("Lewati")

        //Set Color of Skip Button and Next Button
        setColorSkipButton(R.color.black)
        setColorDoneText(R.color.black)
        setNextArrowColor(R.color.black)
        setBackArrowColor(R.color.black)
        setColorDoneText(R.color.black)

        //  For Setup your Indicator
        isIndicatorEnabled = true
        isWizardMode = true
        setIndicatorColor(
            selectedIndicatorColor = MaterialColors.getColor(view, R.attr.colorPrimary),
            unselectedIndicatorColor = MaterialColors.getColor(view, R.attr.sliderTrackPrimaryColor)
        )

        //Asking Permission
        askForPermissions(
            permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            slideNumber = 2,
            required = true
        )
    }

    override fun onUserDeniedPermission(permissionName: String) {
        super.onUserDeniedPermission(permissionName)
        Toast.makeText(this, "Tanpa izin lokasi, beberapa fitur tidak dapat berjalan", Toast.LENGTH_LONG).show()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        AppIntroPreferences(this).isAppIntroducted = true
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val intent = Intent(this, MainActivity::class.java)
        AppIntroPreferences(this).isAppIntroducted = true
        startActivity(intent)
        finish()
    }
}