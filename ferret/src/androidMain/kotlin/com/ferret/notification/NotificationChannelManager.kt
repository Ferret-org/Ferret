package com.ferret.notification

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel as AndroidChannel

internal class NotificationChannelManager(context: Context) {

    private val manager = NotificationManagerCompat.from(context)

    fun ensure(spec: NotificationChannelSpec) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        ensureApi26(spec)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun ensureApi26(spec: NotificationChannelSpec) {
        if (manager.getNotificationChannel(spec.id) != null) return
        manager.createNotificationChannel(
            AndroidChannel(spec.id, spec.name, spec.priority.toImportance())
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun NotificationPriority.toImportance(): Int = when (this) {
        NotificationPriority.MIN     -> NotificationManager.IMPORTANCE_MIN
        NotificationPriority.LOW     -> NotificationManager.IMPORTANCE_LOW
        NotificationPriority.DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
        NotificationPriority.HIGH,
        NotificationPriority.MAX     -> NotificationManager.IMPORTANCE_HIGH
    }
}