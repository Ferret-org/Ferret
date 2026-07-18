@file:OptIn(ExperimentalForeignApi::class)

package com.ferret.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIFont
import platform.UIKit.UILabel
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIView
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.UIPasteboard
import platform.UIKit.NSTextAlignmentCenter
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.dispatch_after
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time

actual fun copyToClipboard(text: String) {
    UIPasteboard.generalPasteboard.string = text
    showToast("Copied")
}

private fun showToast(message: String) {
    val windowScene = UIApplication.sharedApplication
        .connectedScenes
        .mapNotNull { it as? UIWindowScene }
        .firstOrNull { it.activationState == UISceneActivationStateForegroundActive }
        ?: return

    val window = windowScene.windows
        .filterIsInstance<UIWindow>()
        .firstOrNull { it.keyWindow }
        ?: windowScene.windows.filterIsInstance<UIWindow>().firstOrNull()
        ?: return

    val label = UILabel()
    label.text = message
    label.backgroundColor = UIColor.blackColor.colorWithAlphaComponent(0.75)
    label.textColor = UIColor.whiteColor
    label.textAlignment = NSTextAlignmentCenter
    label.layer.cornerRadius = 16.0
    label.clipsToBounds = true
    label.font = UIFont.systemFontOfSize(14.0)
    label.alpha = 0.0
    label.translatesAutoresizingMaskIntoConstraints = false

    window.addSubview(label)

    NSLayoutConstraint.activateConstraints(
        listOf(
            label.widthAnchor.constraintEqualToConstant(120.0),
            label.heightAnchor.constraintEqualToConstant(36.0),
            label.centerXAnchor.constraintEqualToAnchor(window.centerXAnchor),
            label.bottomAnchor.constraintEqualToAnchor(
                anchor = window.safeAreaLayoutGuide.bottomAnchor,
                constant = -40.0,
            ),
        )
    )

    UIView.animateWithDuration(0.3) { label.alpha = 1.0 }

    dispatch_after(
        dispatch_time(DISPATCH_TIME_NOW, 1_500_000_000L),
        dispatch_get_main_queue(),
    ) {
        UIView.animateWithDuration(
            duration = 0.3,
            animations = { label.alpha = 0.0 },
            completion = { _ -> label.removeFromSuperview() },
        )
    }
}
