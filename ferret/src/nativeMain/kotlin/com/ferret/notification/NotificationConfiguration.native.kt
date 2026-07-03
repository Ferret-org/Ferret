package com.ferret.notification

actual class NotificationConfiguration(
    actual val maxBufferSize: Int = NotificationDefaults.DEFAULT_MAX_BUFFER,
    actual val defaultChannel: NotificationChannelSpec = NotificationChannelSpec(),
    actual val defaultPriority: NotificationPriority = NotificationPriority.DEFAULT,
)
