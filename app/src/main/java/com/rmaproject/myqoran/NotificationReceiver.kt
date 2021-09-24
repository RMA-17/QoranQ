package com.rmaproject.myqoran

import android.content.BroadcastReceiver
import android.content.Context
/*
            Code writen by Raka M.A
             */
import android.content.Intent
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.utils.getTargetClass

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val songInfo = intent?.getParcelableExtra<SongInfo?>("songInfo")
        val bundleInfo = intent?.getBundleExtra("bundleInfo")
        val targetClass = bundleInfo?.getString("targetClass")?.getTargetClass()
        if (StarrySky.getActivityStack().isNullOrEmpty()) {
            val mainIntent = Intent(context, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val targetIntent = Intent(context, targetClass)
            targetIntent.putExtra("songId", songInfo?.songId)

            val intents = arrayOf(mainIntent, targetIntent)
            context?.startActivities(intents)
        } else {
            val targetIntent = Intent(context, targetClass)
            targetIntent.putExtra("songId", songInfo?.songId)
            targetIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(targetIntent)
        }
    }
}