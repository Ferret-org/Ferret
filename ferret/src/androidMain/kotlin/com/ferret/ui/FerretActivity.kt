package com.ferret.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ferret.FerretSdk

/**
 * Ferret-owned activity. Opens when the user taps the Ferret notification.
 * Declared in ferret's AndroidManifest so it merges into any host app automatically.
 */
class FerretActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        if (!FerretSdk.isInitialized) {
            showInitializationError()
            return
        }

        setContent {
            FerretRoute()
        }
    }

    private fun showInitializationError() {
        AlertDialog.Builder(this)
            .setTitle("Ferret isn't initialized")
            .setMessage(
                "Initialize Ferret before opening the network inspector."
            )
            .setPositiveButton("OK") { _, _ ->
                finish()
            }
            .setOnCancelListener {
                finish()
            }
            .show()
    }
}

@Composable
private fun FerretRoute() {
    FerretRoute(
        modifier = Modifier,
    )
}
