package com.ferret.notification

import com.ferret.ui.FerretIosUi
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionBadge
import platform.UserNotifications.UNNotificationPresentationOptionList
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

internal class FerretNotificationDelegate : NSObject(), UNUserNotificationCenterDelegateProtocol {

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: UNNotification,
        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit,
    ) {
        withCompletionHandler(
            UNNotificationPresentationOptionList or
                    UNNotificationPresentationOptionSound or
                    UNNotificationPresentationOptionBadge
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
                dispatch_async(dispatch_get_main_queue()) {
                    FerretIosUi.open()
                }
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
