package com.ferret.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.ferret.viewModel.FerretViewModel

/**
 * Ferret-owned activity. Opens when the user taps the Ferret notification.
 * Declared in ferret's AndroidManifest so it merges into any host app automatically.
 */
class FerretActivity : ComponentActivity() {

    private val ferretViewModel: FerretViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.d("Ferret", "onCreate: $this")

        enableEdgeToEdge()
        setContent {
            FerretRoute(ferretViewModel = ferretViewModel)
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
