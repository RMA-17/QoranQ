package com.rmaproject.myqoran.ui.jadwalsholat

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.JadwalSholatLayoutBinding
import com.rmaproject.myqoran.network.`interface`.PrayerTimeAPI
import com.rmaproject.myqoran.ui.settings.PrayerTimePreferences
import kotlinx.coroutines.*
import mumayank.com.airlocationlibrary.AirLocation
import org.joda.time.DateTime
import org.joda.time.Period
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import android.os.Looper.getMainLooper
import android.view.ViewTreeObserver
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.Runnable


class JadwalSholatFragment : Fragment(R.layout.jadwal_sholat_layout) {

//    private lateinit var airLocation: AirLocation
    private val airLocation: AirLocation by lazy {
    //lazy = mengambil data seperti lateinit, cuman dia dipakek 1 kali aja, gak ngeloop
        AirLocation(requireActivity(), object:AirLocation.Callback {

            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
                //Set Empty Value here, filling onFailure causes Failure, really disappointing
            }

            override fun onSuccess(locations: ArrayList<Location>) {
                //LongDef = 107.1833202
                // Lat Def = -6.284734
                val prayerAPI = PrayerTimeAPI.create()
                lifecycleScope.launch {
                    val response = prayerAPI.getPrayerTime(locations[0].longitude, locations[0].latitude)
                    val whereAreYouNow = Geocoder(requireContext(), Locale.getDefault()).getFromLocation(locations[0].latitude, locations[0].longitude, 1).first()
                    if (response.status == "OK") {
                        binding.jadwalSholatList.visibility = View.VISIBLE
                        binding.loadingIndicator.visibility = View.GONE
                        //Akses dari array yang keluar dari hasil JSON
                        val prayerTime = response.results.datetime[0].times
                        Log.d("TIMES", prayerTime.toString())
                        binding.timeShubuh.text = prayerTime.fajr
                        PrayerTimePreferences(requireContext()).waktuShubuhPreferences = prayerTime.fajr
                        binding.timeDzuhur.text = prayerTime.dhuhr
                        PrayerTimePreferences(requireContext()).waktuZuhurPreferences = prayerTime.dhuhr
                        binding.timeAshar.text = prayerTime.asr
                        PrayerTimePreferences(requireContext()).waktuAsharPreferences = prayerTime.asr
                        binding.timeMaghrib.text = prayerTime.maghrib
                        PrayerTimePreferences(requireContext()).waktuMaghribPreferences = prayerTime.maghrib
                        binding.timeIsya.text = prayerTime.isha
                        PrayerTimePreferences(requireContext()).waktuIsyaPreferences = prayerTime.isha
                        binding.locationNow.text = "${whereAreYouNow.locality}, ${whereAreYouNow.subAdminArea}, ${whereAreYouNow.adminArea}, ${whereAreYouNow.countryName}"
//                        AdminArea:Jawa Barat, Sub-admin Area:Kabupaten Bekasi, FeatureName:No.2, Locality: Kecamatan Serang Baru, Premises: null
                    } else {
                        Snackbar.make(requireView(), "Gagal menghubungkan ke server, coba lagi", Snackbar.LENGTH_SHORT)
                            .setTextColor(MaterialColors.getColor(requireView(), R.attr.colorPrimary))
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setAction("Ok") {
                                // Kalau di kotlin ini tidak usah di isi untuk dismiss saja.
                            }
                            .setActionTextColor(MaterialColors.getColor(requireView(), R.attr.colorPrimary))
                            .setBackgroundTint(MaterialColors.getColor(requireView(), R.attr.primary_50))
                            .show()
                        findNavController().navigateUp()
                    }
                }
            }

        }, true)
    }

    private val secondsUpdater by lazy {
        Handler(getMainLooper())
    }

    private val binding:JadwalSholatLayoutBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbarActivity = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbarActivity.title = "Jadwal Sholat"
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav).isVisible = true
        binding.jadwalSholatList.visibility = View.GONE
        setPrayerTime()

        val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.btmAppBar)
        binding.scrollView.setOnScrollChangeListener { _: NestedScrollView, dx: Int, dy: Int, _: Int, _: Int ->
            if (dy > 0) {
                bottomAppBar?.performHide(true)
            } else if (dy < 0) { 
                bottomAppBar?.performShow(true)
            }
        }

        secondsUpdater.postDelayed(object : Runnable {
            override fun run() {
                binding.clockCounter.text = converterToZero(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND))
                secondsUpdater.postDelayed(this, 1000)
            }
        }, 10)

    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            //It will check for both wifi and cellular network
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI)
        }
        return false
    }

    private fun setPrayerTime() {
        //Cek apakah dalam keadaan Online
        if (isOnline(requireContext())) {
            binding.longlatTrack.visibility = View.VISIBLE
            lifecycleScope.launch {
                airLocation.start()
            }
        }
        else if (!isOnline(requireContext())) {
            Toast.makeText(requireContext(), "Koneksi internet diperlukan, mohon aktifkan koneksi internet anda", Toast.LENGTH_SHORT).show()
        }
        // Jika sudah pernah online lakukan:
        else {
            binding.jadwalSholatList.visibility = View.VISIBLE
            binding.loadingIndicator.visibility = View.GONE
            Toast.makeText(requireContext(), "Koneksi internet dibutuhkan untuk update jadwal sholat, kami hanya menampilkan jadwal Sholat yang sudah ada", Toast.LENGTH_LONG).show()
            binding.timeShubuh.text = PrayerTimePreferences(requireContext()).waktuShubuhPreferences
            binding.timeDzuhur.text = PrayerTimePreferences(requireContext()).waktuZuhurPreferences
            binding.timeAshar.text = PrayerTimePreferences(requireContext()).waktuAsharPreferences
            binding.timeMaghrib.text = PrayerTimePreferences(requireContext()).waktuMaghribPreferences
            binding.timeIsya.text = PrayerTimePreferences(requireContext()).waktuIsyaPreferences
        }

//        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//        val timeNow = DateTime(simpleDateFormat.parse(org.joda.time.LocalTime.now().toString()))
//        var period: Period? = null

//        if (timeNow.toString() == PrayerTimePreferences(requireContext()).waktuShubuhPreferences) {
//            period = Period(timeNow, DateTime(simpleDateFormat.parse(PrayerTimePreferences(requireContext()).waktuShubuhPreferences!!.split(" ")[0].trim() + ":00")))
//        } else if (timeNow.toString() == PrayerTimePreferences(requireContext()).waktuZuhurPreferences) {
//            period = Period(timeNow, DateTime(simpleDateFormat.parse(PrayerTimePreferences(requireContext()).waktuZuhurPreferences!!.split(" ")[0].trim() + ":00")))
//        } else if (timeNow.toString() == PrayerTimePreferences(requireContext()).waktuAsharPreferences) {
//            period = Period(timeNow, DateTime(simpleDateFormat.parse(PrayerTimePreferences(requireContext()).waktuAsharPreferences!!.split(" ")[0].trim() + ":00")))
//        } else if (timeNow.toString() == PrayerTimePreferences(requireContext()).waktuMaghribPreferences) {
//            period = Period(timeNow, DateTime(simpleDateFormat.parse(PrayerTimePreferences(requireContext()).waktuMaghribPreferences!!.split(" ")[0].trim() + ":00")))
//        } else if (timeNow.toString() == PrayerTimePreferences(requireContext()).waktuIsyaPreferences) {
//            period = Period(timeNow, DateTime(simpleDateFormat.parse(PrayerTimePreferences(requireContext()).waktuIsyaPreferences!!.split(" ")[0].trim() + ":00")))
//        }
//
//        if (period == null) {
//            return
//        }

//        if (isTimeHasBanded) {
//            coroutineTimerJob = lifecycleScope.launch(Dispatchers.IO) {
//                countDownTimer(this, period.hours, period.minutes, 60 - period.minutes)
//            }
//            isTimeHasBanded = true
//        }
    }

    private fun converterToZero(hour: Int, min: Int, sec: Int): String {

        val formattedHour: String = if (hour <= 9) {
            "0$hour"
        } else {
            hour.toString()
        }
        val formattedMin: String = if (min <= 9) {
            "0$min"
        } else {
            min.toString()
        }
        val formattedSec: String = if (sec <= 9) {
            "0$sec"
        } else {
            sec.toString()
        }

        return "$formattedHour:$formattedMin:$formattedSec"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        airLocation.onActivityResult(requestCode, resultCode, data) // ADD THIS LINE INSIDE onActivityResult
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults) // ADD THIS LINE INSIDE onRequestPermissionResult
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        airLocation.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isOnline(requireContext())) {
            secondsUpdater.removeMessages(0)
        }
    }
}