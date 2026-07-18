package com.ferret.notification


import android.content.Context

actual class NotificationConfiguration(

    val context: Context,

    val defaultSmallIcon: Int = android.R.drawable.ic_dialog_info,

    actual val maxBufferSize: Int = NotificationDefaults.DEFAULT_MAX_BUFFER,

    actual val defaultChannel: NotificationChannelSpec = NotificationChannelSpec(),

    actual val defaultPriority: NotificationPriority = NotificationPriority.HIGH,
)