<div align="center">
# Ferret
<img src="ferret.jpg" width="1000" height="1000" alt="Ferret logo" />
**Ferret out every HTTP request and WebSocket message your app sends and receives.**

[![Maven Central](https://img.shields.io/maven-central/v/io.github.ferret-org/ferret?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.ferret-org/ferret)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-7F52FF)](https://kotlinlang.org)
[![KMP](https://img.shields.io/badge/KMP-Kotlin%20Multiplatform-7F52FF)](https://kotlinlang.org/docs/multiplatform.html)
[![CMP](https://img.shields.io/badge/CMP-Compose%20Multiplatform-4285F4)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-informational)](#getting-started)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue)](#license)

</div>

---

### 📑 Table of Contents

- [Getting Started 👣](#getting-started)
- [Features 🧰](#features)
- [Android Setup 🤖](#android-setup)
- [iOS Setup 🍎](#ios-setup)
- [Using the Inspector 🔍](#using-the-inspector)
- [Configure 🎨](#configure)
- [Notification Permission 🔔](#notification-permission)
- [Sample App 📱](#sample-app)
- [FAQ ❓](#faq)
- [License 📄](#license)

---

Ferret is a **Kotlin Multiplatform** network inspector built on Ktor's `HttpClient`. It plugs into a client you already have, records every HTTP request/response and WebSocket message to a local on-device database, and gives you a system notification, a home-screen shortcut, and a full in-app inspector screen to browse it all.

> Apps using Ferret show a rolling notification summarizing recent network activity. Tapping it opens the full inspector UI. You can also jump into the inspector at any time from a dedicated app shortcut (Android), without needing a notification to tap.

---

<a id="getting-started"></a>
## Getting Started 👣

Ferret is distributed through Maven Central. Add the dependency to your shared/common source set:

**`libs.versions.toml`**
```toml
[versions]
ferret = "1.0.0"

[libraries]
ferret = { module = "io.github.ferret-org:ferret", version.ref = "ferret" }
```

**`build.gradle.kts`**
```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ferret)
        }
    }
}
```

### Requirements

These are the versions Ferret is built and tested with. Kotlin is confirmed to work down to `1.9.0` the other tools (AGP, Gradle, Compose Multiplatform, Ktor) may work with older versions too, but that hasn't been verified yet. `minSdk 28` is the one hard floor here: it's declared in the library's own manifest, so any app consuming Ferret needs a `minSdk` of at least 28.

| Tool | Version |
|---|---|
| Kotlin | 1.9.0+ (built and tested with 2.4.0) |
| Android Gradle Plugin | 9.2.1 |
| Gradle | 9.4.1 |
| Compose Multiplatform | 1.10.x / 1.11.x |
| Ktor | 3.5.1 |
| Android `minSdk` | 28 |
| Android `compileSdk` / `targetSdk` | 37 |
Your project must already use Ktor for networking, Ferret plugs into `HttpClient` directly.

---

<a id="features"></a>
## Features 🧰

- Kotlin Multiplatform: one dependency, works on Android and iOS
- Captures WebSocket connections and messages, not just plain HTTP
- Works out of the box, no proxy or separate debug build needed
- Rolling system notification summarizing recent activity
- Home-screen app shortcut for one-tap access to the inspector (Android)
- Searchable, filterable in-app networkRecord list with full request/response detail
- Share any request as a ready-to-run `curl` command or as a plain-text overview
- Copy any field (header, URL, body) straight to the clipboard
- Clear all captured history from the inspector at any time
- Configurable notification buffer size, priority, channel, and icon

---

<a id="android-setup"></a>
## Android Setup 🤖

Add permissions to your app's `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

Install the plugin on your client:

```kotlin
import com.ferret.intercept.Ferret
import com.ferret.intercept.install
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

val client = HttpClient(OkHttp) {
    install(Ferret) {
        context = applicationContext // an Android Context
    }
}
```

`context` should be an Android `Context` an `Application` context is recommended so it survives configuration changes.

The first time the plugin initializes, Ferret automatically:

1. Creates its Room database on-device to persist captured networkRecords.
2. Creates a notification channel (`"General"` by default) on Android 8+.
3. Registers a dynamic launcher shortcut labeled **"Open Ferret Inspector"**.
4. Starts posting/updating the rolling capture notification as traffic comes in.

Ferret's own inspector activity (`com.ferret.ui.FerretActivity`) is declared in the library's manifest and merges into your app automatically at build time, no manual declaration needed.

### Opening the inspector

- **Notification tap**: tapping the rolling notification opens the inspector directly.
- **Home-screen shortcut**: long-press your app icon and select "Open Ferret Inspector".
- **From your own UI**: launch the activity yourself, e.g. from a debug menu button:
  ```kotlin
  startActivity(Intent(context, Class.forName("com.ferret.ui.FerretActivity")))
  ```

If the inspector is opened before the plugin has initialized, Ferret shows a dialog explaining it isn't ready yet instead of crashing.

---

<a id="ios-setup"></a>
## iOS Setup 🍎

```kotlin
import com.ferret.intercept.Ferret
import com.ferret.intercept.install
import io.ktor.client.*
import io.ktor.client.engine.darwin.*

val client = HttpClient(Darwin) {
    install(Ferret) {}
}
```

No context is required on iOS. Initialization happens automatically the first time the plugin is installed.

There's no OS-level shortcut on iOS, so you decide how the inspector is triggered (a debug menu item, a hidden gesture, a shake handler, etc.), then present the view controller Ferret exposes:

[//]: # (```kotlin)

[//]: # (import com.ferret.ui.ferretViewController)

[//]: # ()
[//]: # (someUIViewController.presentViewController&#40;)

[//]: # (    ferretViewController&#40;&#41;,)

[//]: # (    animated = true,)

[//]: # (    completion = null)

[//]: # (&#41;)

[//]: # (```)

[//]: # ()
[//]: # (From Swift, if `ferretViewController&#40;&#41;` is exposed through your Kotlin/Native framework:)

[//]: # ()
[//]: # (```swift)

[//]: # (let controller = FerretUiKt.ferretViewController&#40;&#41;)

[//]: # (present&#40;controller, animated: true&#41;)

[//]: # (```)
That's it. Ferret will now record every request, response, and WebSocket message made through `client`.

---

<a id="using-the-inspector"></a>
## Using the Inspector 🔍

The inspector UI is shared between Android and iOS and gives you:

- **NetworkRecord list**: every captured HTTP call and WebSocket event, newest first, with method, path, status, and timing at a glance.
- **Search & filter**: filter the list by URL/path, method, or status.
- **Detail screen**: general info (method, URL, status, duration), request headers/body, and response headers/body.
- **Share**: export a request as a runnable `curl` command, or as a plain-text overview for a bug report.
- **Copy to clipboard**: copy any single field directly from the detail screen.
- **Clear history**: wipe all captured networkRecords from the local database.

All of this stays on-device. Nothing captured by Ferret is sent anywhere.

---

<a id="configure"></a>
## Configure 🎨

Ferret is configured through a single `FerretConfiguration` passed to `install(Ferret) { ... }`. Right now it only has one section, notifications, but it's structured so more configuration areas can be added here later without changing how you already use it.

```kotlin
install(Ferret) {
    context = applicationContext // Android only
    configuration = FerretConfiguration(
        notifications = NotificationConfiguration(/* see below */)
    )
}
```

If you don't pass a `configuration`, `FerretConfiguration()` defaults are used.

### Notification configuration
Controls the rolling capture notification: how many recent requests it shows, its priority, its channel, its icon, and whether Ferret requests the notification permission itself.

```kotlin
NotificationConfiguration(
    maxBufferSize = 5,                     // number of recent requests kept in the notification
    defaultPriority = NotificationPriority.HIGH,
    defaultChannel = NotificationChannelSpec(),
    defaultSmallIcon = R.drawable.ic_notification, // Android only
    requestPermission = true               // let Ferret ask for POST_NOTIFICATIONS itself
)
```

| Property | Default | Description |
|---|---|---|
| `maxBufferSize` | `5` | Number of recent requests shown in the rolling notification |
| `defaultPriority` | `NotificationPriority.HIGH` | Priority used for the notification |
| `defaultChannel` | `NotificationChannelSpec()` | The notification channel (id, name, priority) the notification is posted to |
| `defaultSmallIcon` | System default icon | Small icon resource for the notification (Android only) |
| `requestPermission` | See [Notification Permission](#notification-permission) | Whether Ferret prompts for `POST_NOTIFICATIONS` itself, instead of leaving it entirely to your app |

---

### Data retention

Controls how long captured networkRecords stay in Ferret's local Room database before older entries are automatically purged.

```kotlin
FerretConfiguration(
    retentionDurationHours = 24 // keep the last 24 hours of captured traffic, purge anything older
)
```

| Property | Description |
|---|---|
| `retentionDurationHours` | Number of hours of captured networkRecords to retain in the local database; entries older than this are cleared automatically |
 
---

<a id="notification-permission"></a>
## Notification Permission 🔔


Starting with Android 13, apps need `android.permission.POST_NOTIFICATIONS` granted at runtime to show notifications. Ferret keeps capturing traffic even without it, but the rolling notification won't appear until the permission is granted.

By default (`requestPermission = false`), Ferret only declares the permission in its manifest and leaves requesting it up to your app, the same way you'd request it for any of your own notifications (e.g. `ActivityCompat.requestPermissions` or the `rememberLauncherForActivityResult` Compose API).

If you'd rather not wire that up yourself, set `requestPermission = true` on `NotificationConfiguration` (see [Configure](#configure)) and Ferret will show its own dialog asking for `POST_NOTIFICATIONS`:

```kotlin
configuration = FerretConfiguration(
    notifications = NotificationConfiguration(
        requestPermission = true
    )
)
```

<a id="sample-app"></a>
## Sample App 📱

This repository includes a sample Kotlin Multiplatform app under `app/` that wires up Ferret on both Android and iOS.

```bash
# Android
cd app
./gradlew :androidApp:assembleDebug

# iOS
open app/iosApp -a Xcode   # then run from Xcode
```

---

<a id="faq"></a>
## FAQ ❓

**Does Ferret support WebSockets?**

Yes. Ferret captures WebSocket connections and messages in addition to regular HTTP requests and responses, both show up together in the same inspector networkRecord list.

**How do I open the Ferret interface?**

There are two built-in ways, no code needed once the plugin is installed:
- **Notification**: tap the rolling Ferret notification to open the inspector directly.
- **Home-screen shortcut (Android)**: long-press your app's icon and select "Open Ferret Inspector" from the shortcut menu.
  On iOS there's no shortcut, since the OS doesn't support it the same way, so you present `ferretViewController()` from wherever makes sense in your app (a debug menu, a hidden gesture, etc.) as shown in [iOS Setup](#ios-setup).

**Does Ferret work without Ktor?**

No. Ferret is implemented as a Ktor `HttpClient` plugin, so your networking layer needs to go through Ktor.

**Does Ferret slow down my release build?**

Ferret is meant for development and internal builds. If you don't want it in production, gate the `install(Ferret)` call behind a debug flag, or swap in a plain client for release builds.

**Is captured data sent anywhere?**

No. Everything is stored locally in an on-device Room database and never leaves the app.
 
---

<a id="license"></a>
## License 📄

Ferret is licensed under the **Apache License 2.0**.

```
Copyright 2026 The Ferret Authors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```