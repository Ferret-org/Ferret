package com.ferret.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel as AndroidChannel

internal class NotificationChannelManager(context: Context) {

    private val manager = NotificationManagerCompat.from(context)

    fun ensure(spec: NotificationChannelSpec, priority: NotificationPriority): String {
        ensureApi26(spec, priority)
        return spec.id
    }

    private fun ensureApi26(spec: NotificationChannelSpec, priority: NotificationPriority) {
        if (manager.getNotificationChannel(spec.id) != null) return
        manager.createNotificationChannel(
            AndroidChannel(spec.id, spec.name, priority.toImportance())
        )
    }

    private fun NotificationPriority.toImportance(): Int = when (this) {
        NotificationPriority.MIN -> NotificationManager.IMPORTANCE_MIN
        NotificationPriority.LOW -> NotificationManager.IMPORTANCE_LOW
        NotificationPriority.DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
        NotificationPriority.HIGH, NotificationPriority.MAX -> NotificationManager.IMPORTANCE_HIGH
    }
}