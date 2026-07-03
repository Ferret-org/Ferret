package com.ferret.notification


/**
 * Platform-specific notification configuration.
 *
 * Android adds Context and other platform-only values in the actual implementation.
 * Common code only knows about the shared configuration.
 */
expect class NotificationConfiguration {

    val maxBufferSize: Int

    val defaultChannel: NotificationChannelSpec

    val defaultPriority: NotificationPriority
}

/**
 * Default notification channel configuration.
 */
data class NotificationChannelSpec(
    val id: String = NotificationDefaults.DEFAULT_CHANNEL_ID,
    val name: String = NotificationDefaults.DEFAULT_CHANNEL_NAME,
    val priority: NotificationPriority = NotificationPriority.HIGH,
)