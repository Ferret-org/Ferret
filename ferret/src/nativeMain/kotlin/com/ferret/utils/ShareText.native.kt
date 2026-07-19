@file:OptIn(ExperimentalForeignApi::class)

package com.ferret.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UINavigationController
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.popoverPresentationController
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.dispatch_after
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time

// Delay long enough for the Compose Dialog dismiss animation (~300ms) to
// complete before presenting UIActivityViewController. Presenting from a VC
// that is mid-dismissal causes the flicker the user sees.
private const val SHARE_DELAY_NS = 350_000_000L // 350 ms

actual fun shareText(text: String) {
    dispatch_after(
        dispatch_time(DISPATCH_TIME_NOW, SHARE_DELAY_NS),
        dispatch_get_main_queue(),
    ) {
        val topVC = findTopViewController() ?: return@dispatch_after

        val activityVC = UIActivityViewController(
            activityItems = listOf(text),
            applicationActivities = null,
        )

        // Required on iPad — prevents a crash when no sourceView is set
        activityVC.popoverPresentationController?.sourceView = topVC.view

        topVC.presentViewController(
            viewControllerToPresent = activityVC,
            animated = true,
            completion = null,
        )
    }
}

private fun findTopViewController(): UIViewController? {
    val windowScene = UIApplication.sharedApplication
        .connectedScenes
        .mapNotNull { it as? UIWindowScene }
        .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
        ?: return null

    val window = windowScene.windows
        .filterIsInstance<UIWindow>()
        .firstOrNull { it.keyWindow }
        ?: windowScene.windows.filterIsInstance<UIWindow>().firstOrNull()
        ?: return null

    return window.rootViewController?.topViewController()
}

private fun UIViewController.topViewController(): UIViewController {
    presentedViewController?.let { return it.topViewController() }
    return when (this) {
        is UINavigationController -> visibleViewController?.topViewController() ?: this
        is UITabBarController -> selectedViewController?.topViewController() ?: this
        else -> this
    }
}