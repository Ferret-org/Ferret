package com.ferret.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ferret.AndroidContextHolder
import com.ferret.FerretConfiguration
import com.ferret.FerretSdk
import com.ferret.usecase.InitializeFerretUseCase

/**
 * Ferret-owned activity. Opens when the user taps the Ferret notification.
 * Declared in ferret's AndroidManifest so it merges into any host app automatically.
 */
class FerretActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidContextHolder.context = applicationContext
        InitializeFerretUseCase(configuration = FerretConfiguration()).execute()

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

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
        modifier = Modifier.navigationBarsPadding(),
    )
}
