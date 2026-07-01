package com.ferret.notification

import com.ferret.FerretRepository

internal expect class NotificationController(notificationConfiguration: NotificationConfiguration) {
    fun push(model: NotificationModel): Int

    fun dismiss()

    fun dismiss(id: Int)

    fun clear()
}

