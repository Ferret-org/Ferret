package com.ferret.app

import androidx.compose.ui.window.ComposeUIViewController
import com.ferret.app.di.initKoin
import com.ferret.app.home.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}