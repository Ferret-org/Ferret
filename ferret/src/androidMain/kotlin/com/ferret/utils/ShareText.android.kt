package com.ferret.utils

import android.content.Intent
import com.ferret.AndroidContextHolder

actual fun shareText(text: String) {
    val context = AndroidContextHolder.context
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(shareIntent, null).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(chooser)
}