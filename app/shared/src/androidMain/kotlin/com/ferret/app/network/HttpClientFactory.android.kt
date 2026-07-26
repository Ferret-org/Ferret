package com.ferret.app.network

import com.ferret.FerretConfiguration
import com.ferret.NotificationConfiguration
import com.ferret.intercept.Ferret
import com.ferret.intercept.install
import com.ferret.notification.NotificationPriority
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import util.applicationContext
import java.util.concurrent.TimeUnit

actual fun createHttpClient(json: Json): HttpClient = HttpClient(OkHttp) {
    engine {
        config {
            retryOnConnectionFailure(true)
            connectionPool(
                ConnectionPool(
                    maxIdleConnections = 5,
                    keepAliveDuration = 5,
                    timeUnit = TimeUnit.MINUTES
                )
            )
        }
    }
    install(Ferret) {
        context = applicationContext
        configuration = FerretConfiguration(
            notifications = NotificationConfiguration(
                requestPermission = true,
                defaultPriority = NotificationPriority.MIN
            ),
            retentionDurationHours = 12
        )
    }
    configureShared(json)
}
