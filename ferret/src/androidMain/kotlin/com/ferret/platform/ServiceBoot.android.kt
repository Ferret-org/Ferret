package com.ferret.platform

import com.ferret.AndroidContextHolder
import com.ferret.FerretConfiguration
import com.ferret.notification.NotificationConfiguration
import com.ferret.notification.NotificationKit

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
    NotificationKit.push {
        title("Ferret")
        message("Welcome")
    }
}
