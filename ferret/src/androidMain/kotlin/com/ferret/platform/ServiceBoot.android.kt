package com.ferret.platform

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ferret.AndroidContextHolder
import com.ferret.FerretConfiguration
import com.ferret.notification.NotificationConfiguration
import com.ferret.notification.NotificationKit
import com.ferret.utils.FerretShortcut

internal actual fun bootServices(configuration: FerretConfiguration) {
    NotificationKit.boot(
        NotificationConfiguration(
            context = AndroidContextHolder.context,
            defaultSmallIcon = configuration.notifications.defaultSmallIcon,
            maxBufferSize = configuration.notifications.maxBufferSize,
            defaultPriority = configuration.notifications.defaultPriority,
            defaultChannel = configuration.notifications.defaultChannel
        )
    )

    if (configuration.notifications.requestPermission) {
        requestNotificationPermission()
    }

    FerretShortcut.create()
}

private fun requestNotificationPermission() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    if (ContextCompat.checkSelfPermission(
            AndroidContextHolder.context,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
    ) return

    val app = AndroidContextHolder.context.applicationContext as? Application ?: return
    app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
        override fun onActivityResumed(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE,
                )
            }
            app.unregisterActivityLifecycleCallbacks(this)
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}
    })
}

private const val PERMISSION_REQUEST_CODE = 9201