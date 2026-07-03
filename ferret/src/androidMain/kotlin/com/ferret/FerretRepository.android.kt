package com.ferret

import android.content.Context
import com.ferret.notification.NotificationConfiguration
import com.ferret.notification.NotificationKit

internal actual class FerretRepository(

    context: Context,

    configuration: FerretConfiguration

) {
    val context = context.applicationContext

    init {
        NotificationKit.boot(
            configuration = NotificationConfiguration(
                context = this.context,
                defaultSmallIcon = configuration.notifications.defaultSmallIcon,
                maxBufferSize = configuration.notifications.maxBufferSize,
                defaultPriority = configuration.notifications.defaultPriority,
                defaultChannel = configuration.notifications.defaultChannel
            )
        )
    }
}