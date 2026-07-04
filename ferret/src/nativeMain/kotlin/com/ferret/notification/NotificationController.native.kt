package com.ferret.notification

internal actual class NotificationController actual constructor(notificationConfiguration: NotificationConfiguration) {
    actual fun push(model: NotificationModel): Int = 0
    actual fun dismiss() = Unit
    actual fun dismiss(id: Int) = Unit
    actual fun clear() = Unit
}