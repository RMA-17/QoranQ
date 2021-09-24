package com.rmaproject.myqoran

import android.app.Application
import android.app.Notification
import android.os.Bundle
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.notification.INotification
import com.lzx.starrysky.notification.NotificationConfig

class QuranApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val notificationConfig = NotificationConfig.create {
            targetClass { "com.rmaproject.myqoran.NotificationReceiver" }
            targetClassBundle {
                val bundle = Bundle()
                bundle.putString("title", "我是点击通知栏转跳带的参数")
                bundle.putString("targetClass", "com.rmaproject.myqoran.MainActivity")
                return@targetClassBundle bundle
            }
            pendingIntentMode { NotificationConfig.MODE_BROADCAST }
        }
        StarrySky.init(this)
            .setNotificationType(INotification.SYSTEM_NOTIFICATION)
            .setNotificationConfig(notificationConfig)
            .setNotificationSwitch(true)
            .apply()
    }
}