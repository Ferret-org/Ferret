package com.ferret.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.ferret.AndroidContextHolder

actual fun copyToClipboard(text: String) {
    val context = AndroidContextHolder.context
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Ferret", text))
    Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
}
