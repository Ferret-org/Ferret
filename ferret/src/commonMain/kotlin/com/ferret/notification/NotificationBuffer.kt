package com.ferret.notification

internal class NotificationBuffer(private val maxSize: Int) {

    private val entries = ArrayDeque<NotificationModel>()
    private var sequence: Int = NotificationDefaults.BUFFER_NOTIFICATION_ID + 100

    fun push(model: NotificationModel): NotificationModel {
        val stored = if (model.id == 0) model.copy(id = ++sequence) else model
        remove(stored.id)
        entries.addFirst(stored)
        while (entries.size > maxSize) entries.removeLast()
        return stored
    }

    fun remove(id: Int): Boolean = entries.removeAll { it.id == id }

    fun clear() = entries.clear()

    fun snapshot(): List<NotificationModel> = entries.toList()
}