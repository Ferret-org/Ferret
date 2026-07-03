package com.ferret.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.ferret.FerretConfiguration
import com.ferret.FerretSdk
import com.ferret.notification.NotificationChannelSpec
import com.ferret.notification.NotificationKit
import com.ferret.notification.NotificationPriority
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val canPush = mutableStateOf(false)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) canPush.value = true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        FerretSdk.initialize(applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                canPush.value = true
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            canPush.value = true
        }

        setContent {
            val pushing by canPush

            if (pushing) {
                LaunchedEffect(Unit) {
                    var count = 1
                    while (true) {
                        NotificationKit.push {
                            title("GET /api/data")
                            message("200 OK — ${count * 120}ms")
                        }
                        count++
                        delay(3_000)
                    }
                }
            }

            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}