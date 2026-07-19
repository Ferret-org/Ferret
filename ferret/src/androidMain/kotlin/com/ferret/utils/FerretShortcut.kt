package com.ferret.utils

import android.R
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.ferret.AndroidContextHolder
import com.ferret.ui.FerretActivity

internal object FerretShortcut {

    private const val SHORTCUT_ID = "ferret_inspector"

    fun create() {
        val context = AndroidContextHolder.context

        val intent = Intent(context, FerretActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val shortcut = ShortcutInfoCompat.Builder(context, SHORTCUT_ID)
            .setShortLabel("Ferret")
            .setLongLabel("Open Ferret Inspector")
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_dialog_info))
            .setIntent(intent)
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }

    fun remove() {
        ShortcutManagerCompat.removeDynamicShortcuts(
            AndroidContextHolder.context,
            listOf(SHORTCUT_ID),
        )
    }
}