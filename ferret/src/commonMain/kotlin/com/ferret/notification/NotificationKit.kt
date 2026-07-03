package com.ferret.notification

import kotlin.concurrent.Volatile

/**
 * The single public notification entry point.
 *
 * Initialize Ferret once at app startup, then use this object freely:
 * ```kotlin
 * // Application.onCreate()
 * FerretSdk.initialize(applicationContext)
 *
 * // Anywhere afterwards
 * NotificationKit.push { title("GET /users"); message("200 OK") }
 * ```
 */
object NotificationKit {

    @Volatile
    private var controller: NotificationController? = null

    private val active: NotificationController
        get() = controller ?: error("NotificationKit not initialized. Call FerretSdk.initialize() first.")

    internal fun boot(configuration: NotificationConfiguration) {
        if (controller != null) return
        controller = NotificationController(configuration)
    }

    /** Append a buffered entry and rebuild the single notification. Returns entry id. */
    fun push(block: NotificationBuilder.() -> Unit): Int =
        active.push(NotificationBuilder().apply(block).build())

    /** Remove the buffered notification from the system tray. */
    fun dismiss() = active.dismiss()

    /** Remove a single entry from the buffer and rebuild. */
    fun dismiss(id: Int) = active.dismiss(id)

    /** Empty the buffer and remove the notification. */
    fun clear() = active.clear()
}