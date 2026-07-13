package com.ferret.ui

import androidx.compose.ui.window.ComposeUIViewController
import com.ferret.ui.navigation.FerretNavigation
import platform.UIKit.UIViewController

fun ferretViewController(): UIViewController =
    ComposeUIViewController {
        FerretNavigation()
    }