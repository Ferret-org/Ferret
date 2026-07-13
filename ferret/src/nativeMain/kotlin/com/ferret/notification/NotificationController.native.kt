package com.ferret.notification

import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationInterruptionLevel
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

internal actual class NotificationController actual constructor(
    notificationConfiguration: NotificationConfiguration,
) {

    private val configuration = notificationConfiguration

    private val notificationCenter =
        UNUserNotificationCenter.currentNotificationCenter()

    private val notificationDelegate =
        FerretNotificationDelegate()

    private val buffer = NotificationBuffer(
        maxSize = configuration.maxBufferSize,
    )

    init {
        notificationCenter.delegate = notificationDelegate
    }

    actual fun push(model: NotificationModel): Int {
        val stored = buffer.push(model)
        rebuild()
        return stored.id
    }

    actual fun dismiss() {
        cancel()
    }

    actual fun dismiss(id: Int) {
        if (buffer.remove(id)) {
            rebuild()
        }
    }

    actual fun clear() {
        buffer.clear()
        cancel()
    }

    private fun rebuild() {
        val entries = buffer.snapshot()

        if (entries.isEmpty()) {
            cancel()
            return
        }

        post(
            identifier = NotificationDefaults.BUFFER_NOTIFICATION_ID.toString(),
            entries = entries,
        )
    }

    private fun post(
        identifier: String,
        entries: List<NotificationModel>,
    ) {

        val content = render(entries)

        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = identifier,
            content = content,
            trigger = null,
        )

        notificationCenter.addNotificationRequest(
            request = request,
            withCompletionHandler = { error ->
                if (error != null) {
                    println(
                        "Ferret iOS notification error: " +
                                error.localizedDescription
                    )
                } else {
                    println(
                        "Ferret iOS notification successfully scheduled"
                    )
                }
            },
        )
    }

    private fun render(
        entries: List<NotificationModel>,
    ): UNMutableNotificationContent {
        val newest = entries.first()

        val content = UNMutableNotificationContent()

        content.setTitle(newest.title)

        content.setBody(
            entries.joinToString(
                separator = "\n",
            ) { entry ->
                "${entry.title}  ·  ${entry.message}"
            }
        )


        content.setInterruptionLevel(
            configuration.defaultPriority.toInterruptionLevel()
        )

        content.setUserInfo(
            mapOf(
                FerretNotificationKeys.TYPE to
                        FerretNotificationKeys.TYPE_FERRET,
            )
        )

        return content
    }

    private fun cancel() {
        val identifier =
            NotificationDefaults.BUFFER_NOTIFICATION_ID.toString()

        notificationCenter
            .removePendingNotificationRequestsWithIdentifiers(
                listOf(identifier)
            )

        notificationCenter
            .removeDeliveredNotificationsWithIdentifiers(
                listOf(identifier)
            )
    }
}

private fun NotificationPriority.toInterruptionLevel():
        UNNotificationInterruptionLevel =
    when (this) {
        NotificationPriority.MIN ->
            UNNotificationInterruptionLevel.UNNotificationInterruptionLevelPassive

        NotificationPriority.LOW ->
            UNNotificationInterruptionLevel.UNNotificationInterruptionLevelPassive

        NotificationPriority.DEFAULT,
        NotificationPriority.HIGH,
        NotificationPriority.MAX ->
            UNNotificationInterruptionLevel.UNNotificationInterruptionLevelActive
    }