package com.ferret.notification

import com.ferret.ui.FerretIosUi
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

internal class FerretNotificationDelegate :
    NSObject(),
    UNUserNotificationCenterDelegateProtocol {

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: UNNotification,
        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit,
    ) {
        withCompletionHandler(
            UNAuthorizationOptionAlert or
                    UNAuthorizationOptionSound or
                    UNAuthorizationOptionBadge
        )
    }

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        didReceiveNotificationResponse: UNNotificationResponse,
        withCompletionHandler: () -> Unit,
    ) {
        try {
            val userInfo =
                didReceiveNotificationResponse
                    .notification
                    .request
                    .content
                    .userInfo

            val isFerretNotification =
                userInfo[
                    FerretNotificationKeys.TYPE
                ]?.toString() ==
                        FerretNotificationKeys.TYPE_FERRET

            if (isFerretNotification) {
                FerretIosUi.open()
            }
        } catch (throwable: Throwable) {
            println(
                "Ferret iOS: notification tap failed: ${throwable.message}"
            )
        } finally {
            withCompletionHandler()
        }
    }

}