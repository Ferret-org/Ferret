package com.ferret.utils

import kotlinx.datetime.TimeZone
import kotlin.time.Instant
import kotlinx.datetime.toLocalDateTime

fun Long.formatTime(): String {
    val local = Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    val hour24 = local.hour
    val minute = local.minute

    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }

    val period = if (hour24 < 12) "AM" else "PM"

    val minuteString = minute.toString().padStart(2, '0')

    return "$hour12:$minuteString $period"
}


fun Long.formatBytes(): String = when {
    this < 1024 -> "$this B"
    this < 1024 * 1024 -> "${this / 1024} KB"
    this < 1024 * 1024 * 1024 -> "${this / (1024 * 1024)} MB"
    else -> "${this / (1024 * 1024 * 1024)} GB"
}