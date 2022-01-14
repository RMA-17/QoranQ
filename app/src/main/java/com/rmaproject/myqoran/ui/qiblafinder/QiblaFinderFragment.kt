package com.rmaproject.myqoran.ui.qiblafinder

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rmaproject.myqoran.R
import com.rmaproject.myqoran.databinding.QiblaFinderWebviewLayoutBinding
import android.os.Build

import android.webkit.PermissionRequest
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar

import com.just.agentweb.AgentWeb

class QiblaFinderFragment : Fragment(R.layout.qibla_finder_webview_layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Jika ini adalah webView, maka untuk load website nya kita harus memasukkan URL terlebih dahulu
//        binding.webView.loadUrl("https://qiblafinder.withgoogle.com/intl/id/")

//        binding.webView.settings.javaScriptEnabled = true

//        binding.webView.settings.setSupportZoom(true)

        //Tapi semua itu tidak perlu sih, soalnya pakek library AgentWeb
        AgentWeb.with(this)
            .setAgentWebParent((view as RelativeLayout), RelativeLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go("https://qiblafinder.withgoogle.com/intl/en/")


        val toolbarActivity = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbarActivity.title = "Qibla Finder"

    }
}