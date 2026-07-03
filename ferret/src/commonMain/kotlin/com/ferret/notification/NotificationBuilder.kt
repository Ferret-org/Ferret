package com.ferret.notification

class NotificationBuilder internal constructor() {

    private var title: String = ""
    private var message: String = ""
    private var extras: Map<String, String> = emptyMap()

    fun title(value: String) = apply { title = value }
    fun message(value: String) = apply { message = value }
    fun extras(value: Map<String, String>) = apply { extras = value }

    internal fun build(id: Int = 0): NotificationModel = NotificationModel(
        id = id,
        title = title,
        message = message,
        extras = extras,
    )
}