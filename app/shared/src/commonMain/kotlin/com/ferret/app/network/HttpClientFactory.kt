package com.ferret.app.network

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val json: Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
}

/** Platform-specific engine creation (OkHttp on Android, Darwin on iOS). */
expect fun createHttpClient(json: Json): HttpClient

/** Shared plugin configuration applied by each platform's actual. */
fun HttpClientConfig<*>.configureShared(json: Json) {
    install(ContentNegotiation) {
        json(json)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 30_000
    }
    install(Logging) {
        level = LogLevel.INFO
        logger = object : io.ktor.client.plugins.logging.Logger {
            override fun log(message: String) {
                Logger.i(tag = "Ktor") { message }
            }
        }
    }
}
