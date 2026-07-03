package com.ferret.notification

/**
 * Platform-neutral priority. Mapped to NotificationCompat priority + channel importance on Android,
 * and to interruption levels on iOS. Part of the model — not an abstraction layer.
 */
enum class NotificationPriority { MIN, LOW, DEFAULT, HIGH, MAX }
