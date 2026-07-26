package com.ferret

import com.ferret.notification.NotificationChannelSpec
import com.ferret.notification.NotificationPriority

data class FerretConfiguration(
    val notifications: NotificationConfiguration = NotificationConfiguration(),
    val retentionDurationHours: Long = 12
)

data class NotificationConfiguration(
    val maxBufferSize: Int = 5,
    val defaultPriority: NotificationPriority = NotificationPriority.HIGH,
    val defaultChannel: NotificationChannelSpec = NotificationChannelSpec(),
    val defaultSmallIcon: Int = 0,
    val requestPermission: Boolean = false
)