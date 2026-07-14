package com.ferret.shortcut

import android.R
import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.ferret.AndroidContextHolder
import com.ferret.ui.FerretActivity

private const val FERRET_SHORTCUT_ID = "ferret_inspector"

internal fun addFerretShortcut() {
    val context = AndroidContextHolder.context
    val intent = Intent(context, FerretActivity::class.java).apply {
        action = Intent.ACTION_VIEW
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    val shortcut = ShortcutInfoCompat.Builder(context, FERRET_SHORTCUT_ID)
        .setShortLabel("Ferret")
        .setLongLabel("Ferret Inspector")
        .setIcon(IconCompat.createWithResource(context, R.drawable.ic_menu_add))
        .setIntent(intent)
        .build()

    ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
}

internal fun removeFerretShortcut(context: Context) {
    ShortcutManagerCompat.removeDynamicShortcuts(context, listOf(FERRET_SHORTCUT_ID))
}
