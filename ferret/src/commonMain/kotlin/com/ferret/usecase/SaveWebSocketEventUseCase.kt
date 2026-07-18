package com.ferret.usecase

import com.ferret.model.NetworkRecord
import com.ferret.model.WebSocketEvent
import com.ferret.repository.TransactionRepository

internal class SaveWebSocketEventUseCase(
    private val repository: TransactionRepository
) {
    suspend fun save(event: WebSocketEvent) {
        repository.insert(event.toNetworkRecord())
    }

    private fun WebSocketEvent.toNetworkRecord(): NetworkRecord = when (this) {
        is WebSocketEvent.Connected -> NetworkRecord(
            sessionId = connectionId,
            requestDate = timestamp,
            protocol = if (url.startsWith("wss")) "WSS" else "WS",
            method = "CONNECTED",
            url = url,
            host = wsHost(url),
            path = wsPath(url),
            scheme = if (url.startsWith("wss")) "wss" else "ws",
            responseDate = timestamp,
            tookMs = 0,
            responseCode = 101,
            responseMessage = "Switching Protocols",
        )

        is WebSocketEvent.FrameReceived -> NetworkRecord(
            sessionId = connectionId,
            requestDate = timestamp,
            protocol = "WS",
            method = "FRAME_IN",
            url = url,
            host = "",
            path = "",
            scheme = "ws",
            responseBody = data,
            responsePayloadSize = sizeBytes,
            requestContentType = frameType,
        )

        is WebSocketEvent.FrameSent -> NetworkRecord(
            sessionId = connectionId,
            requestDate = timestamp,
            protocol = "WS",
            method = "FRAME_OUT",
            url = url,
            host = "",
            path = "",
            scheme = "ws",
            requestBody = data,
            requestPayloadSize = sizeBytes,
            requestContentType = frameType,
        )

        is WebSocketEvent.Disconnected -> NetworkRecord(
            sessionId = connectionId,
            requestDate = timestamp,
            protocol = "WS",
            method = "DISCONNECTED",
            url = url,
            host = "",
            path = "",
            scheme = "ws",
            responseDate = timestamp,
            tookMs = tookMs,
        )
    }

    private fun wsHost(url: String) =
        url.removePrefix("wss://").removePrefix("ws://").substringBefore("/")

    private fun wsPath(url: String): String {
        val afterScheme = url.removePrefix("wss://").removePrefix("ws://")
        val afterHost = afterScheme.substringAfter("/", "")
        return if (afterHost.isEmpty()) "/" else "/$afterHost"
    }
}
