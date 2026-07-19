package com.ferret.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

internal actual class NotificationController actual constructor(
    notificationConfiguration: NotificationConfiguration
) {
    private val configuration = notificationConfiguration

    private val context: Context = configuration.context.applicationContext
    private val managerCompat = NotificationManagerCompat.from(context)
    private val systemManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val buffer = NotificationBuffer(configuration.maxBufferSize)
    private val channels = NotificationChannelManager(context)
    private val pendingIntents = PendingIntentFactory(context)

    init {
        channels.ensure(configuration.defaultChannel)
    }

    actual fun push(model: NotificationModel): Int {
        val stored = buffer.push(model)
        rebuild()
        return stored.id
    }

    actual fun dismiss() = cancel(NotificationDefaults.BUFFER_NOTIFICATION_ID)

    actual fun dismiss(id: Int) {
        if (buffer.remove(id)) rebuild()
    }

    actual fun clear() {
        buffer.clear()
        cancel(NotificationDefaults.BUFFER_NOTIFICATION_ID)
    }

    private fun rebuild() {
        val entries = buffer.snapshot()
        if (entries.isEmpty()) {
            cancel(NotificationDefaults.BUFFER_NOTIFICATION_ID)
            return
        }
        post(NotificationDefaults.BUFFER_NOTIFICATION_ID, render(entries))
    }

    private fun render(entries: List<NotificationModel>): Notification {
        val newest = entries.first()
        val builder = NotificationCompat.Builder(context, configuration.defaultChannel.id)
            .setSmallIcon(resolveSmallIcon())
            .setContentTitle(newest.title)
            .setContentText(newest.message)
            .setContentIntent(
                pendingIntents.create(
                    extras = newest.extras,
                    requestCode = newest.id
                )
            )
            .setPriority(configuration.defaultPriority.toCompatPriority())
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        val inbox = NotificationCompat.InboxStyle()
        entries.forEach { inbox.addLine("${it.title}  ·  ${it.message}") }
        builder.setStyle(inbox)

        builder.setSubText(entries.size.toString())
        return builder.build()
    }

    @SuppressLint("MissingPermission")
    private fun post(id: Int, notification: Notification) {
        if (!areNotificationsEnabled()) return
        managerCompat.notify(id, notification)
    }

    private fun cancel(id: Int) = managerCompat.cancel(id)

    private fun areNotificationsEnabled(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            systemManager.areNotificationsEnabled()
        } else true

    private fun resolveSmallIcon(): Int =
        if (configuration.defaultSmallIcon != 0) configuration.defaultSmallIcon
        else android.R.drawable.ic_dialog_info

    private fun NotificationPriority.toCompatPriority(): Int = when (this) {
        NotificationPriority.MIN -> NotificationCompat.PRIORITY_MIN
        NotificationPriority.LOW -> NotificationCompat.PRIORITY_LOW
        NotificationPriority.DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
        NotificationPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
        NotificationPriority.MAX -> NotificationCompat.PRIORITY_MAX
    }
}
