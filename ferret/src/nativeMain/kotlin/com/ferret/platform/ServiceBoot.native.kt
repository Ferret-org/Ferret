package com.ferret.platform

import com.ferret.FerretConfiguration
import com.ferret.notification.NotificationConfiguration
import com.ferret.notification.NotificationKit

internal actual fun bootServices(configuration: FerretConfiguration) {
    NotificationKit.boot(
        NotificationConfiguration()
    )
}

// TODO() remove this after testing
object IosNotificationTest {

    fun show() {
        println("Ferret iOS: testing notification")

        NotificationKit.boot(
            NotificationConfiguration()
        )

        NotificationKit.push {
            title("Ferret")
            message("iOS notification is working")
        }
    }
}