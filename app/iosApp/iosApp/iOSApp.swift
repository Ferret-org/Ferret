import SwiftUI
import UserNotifications
import Shared

@main
struct iOSApp: App {

    private let notificationDelegate = NotificationDelegate()

    init() {
        UNUserNotificationCenter.current().delegate =
            notificationDelegate
    }


    var body: some Scene {
        WindowGroup {
            ContentView()
                .task {
                    await requestNotificationPermission()
                }

        }
    }
}


private func requestNotificationPermission() async {
    do {
        let granted = try await UNUserNotificationCenter.current()
            .requestAuthorization(
                options: [.alert, .badge, .sound]
            )

        print("Notification permission granted: \(granted)")
    } catch {
        print(
            "Notification permission error: \(error.localizedDescription)"
        )
    }
}

final class NotificationDelegate: NSObject, UNUserNotificationCenterDelegate {

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler:
            @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        print("Foreground notification received")

        completionHandler([.banner, .list, .sound])
    }
}
