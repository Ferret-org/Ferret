package com.ferret.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Ferret-owned activity. Opens when the user taps the Ferret notification.
 * Declared in ferret's AndroidManifest so it merges into any host app automatically.
 */
class FerretActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.d("Ferret", "onCreate: $this")

        enableEdgeToEdge()
        setContent {
            FerretRoute()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        android.util.Log.d("Ferret", "onNewIntent: $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        android.util.Log.d("Ferret", "onDestroy: $this")
    }
}

@Composable
private fun FerretRoute() {
    FerretScreen(
        modifier = Modifier,
    )
}
