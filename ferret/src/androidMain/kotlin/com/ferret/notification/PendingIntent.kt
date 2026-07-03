package com.ferret.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.ferret.ui.FerretActivity

internal class PendingIntentFactory(
    private val context: Context
) {

    private val flags =
        PendingIntent.FLAG_UPDATE_CURRENT or
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE
                else 0

    fun create(
        requestCode: Int,
        extras: Map<String, String> = emptyMap()
    ): PendingIntent {

        val intent = Intent(context, FerretActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            extras.forEach { (key, value) ->
                putExtra(key, value)
            }
        }

        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            flags
        )
    }
}