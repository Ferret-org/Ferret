package com.ferret.app.network

import android.content.Context
import com.ferret.FerretSdk
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
    configureShared(json)

    FerretSdk.initialize(context = applicationContext)
}
