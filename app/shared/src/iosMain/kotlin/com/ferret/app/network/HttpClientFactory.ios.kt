package com.ferret.app.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.serialization.json.Json

actual fun createHttpClient(json: Json): HttpClient = HttpClient(Darwin) {
    engine {
        configureSession {
            timeoutIntervalForRequest = 30.0
            timeoutIntervalForResource = 60.0
            waitsForConnectivity = true
        }
    }
    install(WebSockets)
    configureShared(json)
}
