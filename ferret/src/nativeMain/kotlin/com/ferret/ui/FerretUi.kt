package com.ferret.ui

import com.ferret.FerretSdk
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UINavigationController
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

internal object FerretIosUi {

    fun open() {
        val presenter = findTopViewController()
            ?: return

        if (!FerretSdk.isInitialized) {
            showInitializationError(presenter)
            return
        }

        val ferretViewController =
            ferretViewController()

        presenter.presentViewController(
            viewControllerToPresent = ferretViewController,
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
                scene.activationState ==
                        UISceneActivationStateForegroundActive
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