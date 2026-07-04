package com.ferret.notification

data class NotificationModel(
    val id: Int = 0,
    val title: String,
    val message: String,
    val extras: Map<String, String> = emptyMap()
)