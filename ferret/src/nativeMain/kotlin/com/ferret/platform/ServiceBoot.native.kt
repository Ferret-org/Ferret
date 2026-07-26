package com.ferret.platform

import com.ferret.FerretConfiguration
import com.ferret.notification.NotificationConfiguration
import com.ferret.notification.NotificationKit
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

internal actual fun bootServices(configuration: FerretConfiguration) {
    NotificationKit.boot(
        NotificationConfiguration(
            maxBufferSize = configuration.notifications.maxBufferSize,
            defaultPriority = configuration.notifications.defaultPriority,
            defaultChannel = configuration.notifications.defaultChannel,
        )
    )

    if (configuration.notifications.requestPermission) {
        UNUserNotificationCenter.currentNotificationCenter()
            .requestAuthorizationWithOptions(
                UNAuthorizationOptionAlert or
                        UNAuthorizationOptionSound or
                        UNAuthorizationOptionBadge
            ) { granted, error ->
                if (error != null) {
                    println("Ferret iOS: notification permission error: ${error.localizedDescription}")
                } else {
                    println("Ferret iOS: notification permission granted=$granted")
                }
            }
    }
}