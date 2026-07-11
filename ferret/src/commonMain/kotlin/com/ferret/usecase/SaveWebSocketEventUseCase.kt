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
            sessionId = "$connectionId-connect",
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
            sessionId = "$connectionId-in-$count",
            requestDate = timestamp,
            protocol = "WS",
            method = "FRAME_IN",
            url = connectionId,
            host = "",
            path = "",
            scheme = "ws",
            responseBody = data,
            responsePayloadSize = sizeBytes,
            requestContentType = frameType,
        )

        is WebSocketEvent.FrameSent -> NetworkRecord(
            sessionId = "$connectionId-out-$count",
            requestDate = timestamp,
            protocol = "WS",
            method = "FRAME_OUT",
            url = connectionId,
            host = "",
            path = "",
            scheme = "ws",
            requestBody = data,
            requestPayloadSize = sizeBytes,
            requestContentType = frameType,
        )

        is WebSocketEvent.Disconnected -> NetworkRecord(
            sessionId = "$connectionId-close",
            requestDate = timestamp,
            protocol = "WS",
            method = "DISCONNECTED",
            url = connectionId,
            host = "",
            path = "",
            scheme = "ws",
            responseDate = timestamp,
            tookMs = 0,
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
