@file:OptIn(ExperimentalForeignApi::class)

package com.ferret.ui

import com.ferret.FerretSdk
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIBarButtonItemStyle
import platform.UIKit.UIImage
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UINavigationController
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UISceneActivationStateForegroundInactive
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.navigationItem
import platform.darwin.NSObject

internal object FerretIosUi {

    // Held to prevent UIBarButtonItem from losing its weak target reference
    private var closeButtonHandler: CloseButtonHandler? = null

    fun open() {
        val presenter = findTopViewController()
            ?: return

        if (!FerretSdk.isInitialized) {
            showInitializationError(presenter)
            return
        }

        val ferretVc = ferretViewController()
        val navController = UINavigationController(rootViewController = ferretVc)
        navController.modalPresentationStyle = UIModalPresentationFullScreen

        val handler = CloseButtonHandler(navController)
        closeButtonHandler = handler

        ferretVc.navigationItem.leftBarButtonItem = UIBarButtonItem(
            image = UIImage.systemImageNamed("chevron.left"),
            style = UIBarButtonItemStyle.UIBarButtonItemStylePlain,
            target = handler,
            action = NSSelectorFromString("onClose"),
        )

        presenter.presentViewController(
            viewControllerToPresent = navController,
            animated = true,
            completion = null,
        )
    }

    private fun showInitializationError(
        presenter: UIViewController,
    ) {
        val alert =
            UIAlertController.alertControllerWithTitle(
                title = "Ferret isn't initialized",
                message = "Initialize Ferret before opening the network inspector.",
                preferredStyle = UIAlertControllerStyleAlert,
            )

        alert.addAction(
            UIAlertAction.actionWithTitle(
                title = "OK",
                style = UIAlertActionStyleDefault,
                handler = null,
            )
        )

        presenter.presentViewController(
            viewControllerToPresent = alert,
            animated = true,
            completion = null,
        )
    }

    private fun findTopViewController(): UIViewController? {
        val windowScene = UIApplication.sharedApplication
            .connectedScenes
            .mapNotNull { scene ->
                scene as? UIWindowScene
            }
            .firstOrNull { scene ->
                scene.activationState == UISceneActivationStateForegroundActive ||
                        scene.activationState == UISceneActivationStateForegroundInactive
            }
            ?: return null

        val window = windowScene.windows
            .filterIsInstance<UIWindow>()
            .firstOrNull { window ->
                window.keyWindow
            }
            ?: windowScene.windows
                .filterIsInstance<UIWindow>()
                .firstOrNull()
            ?: return null

        return window.rootViewController
            ?.topViewController()
    }

    private fun UIViewController.topViewController(): UIViewController {
        presentedViewController?.let { presented ->
            return presented.topViewController()
        }

        when (this) {
            is UINavigationController -> {
                visibleViewController?.let { visible ->
                    return visible.topViewController()
                }
            }

            is UITabBarController -> {
                selectedViewController?.let { selected ->
                    return selected.topViewController()
                }
            }
        }

        return this
    }
}

private class CloseButtonHandler(
    private val navController: UINavigationController,
) : NSObject() {
    @ObjCAction
    fun onClose() {
        navController.dismissViewControllerAnimated(true, completion = null)
    }
}