package com.rmaproject.myqoran

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.rmaproject.myqoran.databinding.ActivityMainBinding
import com.rmaproject.myqoran.ui.home.HomeFragment
import com.rmaproject.myqoran.ui.home.QuranViewModel
import com.rmaproject.myqoran.ui.jadwalsholat.JadwalSholatFragment
import com.rmaproject.myqoran.ui.quran.read.surah.FragmentQuranRead
import com.rmaproject.myqoran.ui.settings.FragmentSettings
import com.rmaproject.myqoran.ui.settings.ThemePreferences

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var appBarConfiguration: AppBarConfiguration
    val binding:ActivityMainBinding by viewBinding(R.id.drawer_layout)
    private val darkIndicator by lazy {binding.navView.menu.findItem(R.id.darkModeSwitcher)}
    private val switchTheme by lazy {darkIndicator.actionView as SwitchCompat}
    private val viewModel:QuranViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        checkTheme()

        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val bottomNavigation = binding.bottomNav

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,R.id.nav_jadwal_sholat, R.id.nav_settings
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            var showToolbar = true
            var showBottomNav = true
            when(destination.id) {
                R.id.searchFragment ->{
                    showBottomNav = false
                    showToolbar = false
                }
                R.id.searchFragmentSurah -> {
                    showToolbar = false
                    showBottomNav = false
                }
                R.id.nav_read_quran -> {
                    showBottomNav = false
                    switchTheme.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            ThemePreferences(this).darkMode = FragmentSettings.DarkModeOn
                            darkIndicator.title = "Mode Gelap"
                            darkIndicator.setIcon(R.drawable.ic_baseline_dark_mode_24)
                        }
                        else{
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            ThemePreferences(this).darkMode = FragmentSettings.DarkModeOff
                            darkIndicator.title = "Mode Terang"
                            darkIndicator.setIcon(R.drawable.ic_baseline_brightness_low_24)
                        }
                    }
                }
                R.id.nav_about -> {
                    showBottomNav = false
                }
                R.id.nav_give_rating -> {
                    showBottomNav = false
                }
                R.id.fragmentBookmark -> {
                    showBottomNav = false
                }
                R.id.nav_qibla -> {
                    showBottomNav = false
                }
                R.id.nav_help -> {
                    showBottomNav = false
                }
                R.id.nav_home -> {
                    binding.toolbar.title = "MyQor'an"
                    switchThemeDoL(R.id.nav_home, HomeFragment(), navController)
                }
                R.id.nav_settings -> {
                    switchThemeDoL(R.id.nav_settings, FragmentSettings(), navController)
                }
                R.id.nav_jadwal_sholat -> {
                    switchThemeDoL(R.id.nav_jadwal_sholat, JadwalSholatFragment(), navController)
                }
                else -> {
                    showToolbar = true
                    switchTheme.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            ThemePreferences(this).darkMode = FragmentSettings.DarkModeOn
                            darkIndicator.title = "Mode Gelap"
                            darkIndicator.setIcon(R.drawable.ic_baseline_dark_mode_24)
                        }
                        else{
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            ThemePreferences(this).darkMode = FragmentSettings.DarkModeOff
                            darkIndicator.title = "Mode Terang"
                            darkIndicator.setIcon(R.drawable.ic_baseline_brightness_low_24)
                        }
                    }
                }
            }
            binding.toolbar.isVisible = showToolbar
            binding.btmAppBar.isVisible = showBottomNav

        }

        when(ThemePreferences(this).darkMode){
            FragmentSettings.DarkModeOn -> {
                switchTheme.isChecked = true
                darkIndicator.title = "Mode Terang"
                darkIndicator.setIcon(R.drawable.ic_baseline_brightness_low_24)
            }

            FragmentSettings.DarkModeOff -> {
                switchTheme.isChecked = false
                darkIndicator.title = "Mode Gelap"
                darkIndicator.setIcon(R.drawable.ic_baseline_dark_mode_24)
            }
        }
        navView.getHeaderView(0).findViewById<TextView>(R.id.versionApp).text = "Version " + BuildConfig.VERSION_NAME
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkTheme() {
        when (ThemePreferences(this).darkMode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.applyDayNight()
            }
        }     //Jika di Activity, context memakai 'this'
        when (ThemePreferences(this).changeAccent) {
            DefaultAccent -> {
                setTheme(R.style.Theme_MyQoran)
            }
            BlueAccent -> {
                setTheme(R.style.Theme_MyQoran_Blue)
            }
            GreenAccent -> {
                setTheme(R.style.Theme_MyQoran_Green)
            }
            RedAccent -> {
                setTheme(R.style.Theme_MyQoran_Red)
            }
        }
    }

    private fun switchThemeDoL(fragmentId:Int, fragment:Fragment, navController: NavController) {
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                ThemePreferences(this).darkMode = FragmentSettings.DarkModeOn
                darkIndicator.title = "Mode Gelap"
                darkIndicator.setIcon(R.drawable.ic_baseline_dark_mode_24)
                navController.navigate(fragmentId, fragment.arguments, NavOptions.Builder().setPopUpTo(fragmentId, true).build())
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                ThemePreferences(this).darkMode = FragmentSettings.DarkModeOff
                darkIndicator.title = "Mode Terang"
                darkIndicator.setIcon(R.drawable.ic_baseline_brightness_low_24)
                navController.navigate(fragmentId, fragment.arguments, NavOptions.Builder().setPopUpTo(fragmentId, true).build())
            }
        }
    }


    companion object{
        const val DefaultAccent = 0
        const val BlueAccent = 1
        const val GreenAccent = 2
        const val RedAccent = 3
    }
}