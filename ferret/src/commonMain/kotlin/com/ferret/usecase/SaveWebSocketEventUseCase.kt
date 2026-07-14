package com.ferret.usecase

import com.ferret.model.NetworkRecord
import com.ferret.model.WebSocketEvent
import com.ferret.repository.TransactionRepository

internal class SaveWebSocketEventUseCase(
    private val repository: TransactionRepository
) {
    suspend fun save(event: WebSocketEvent) {
        when (event) {
            is WebSocketEvent.Connected -> repository.insert(
                NetworkRecord(
                    sessionId = event.connectionId,
                    requestDate = event.timestamp,
                    protocol = if (event.url.startsWith("wss")) "WSS" else "WS",
                    method = "CONNECTED",
                    url = event.url,
                    host = wsHost(event.url),
                    path = wsPath(event.url),
                    scheme = if (event.url.startsWith("wss")) "wss" else "ws",
                    responseCode = 101,
                    responseMessage = "Switching Protocols",
                )
            )

            is WebSocketEvent.FrameReceived -> repository.updateWsFrameIn(
                sessionId = event.connectionId,
                responseBody = event.data,
                responsePayloadSize = event.sizeBytes,
            )

            is WebSocketEvent.FrameSent -> repository.updateWsFrameOut(
                sessionId = event.connectionId,
                requestBody = event.data,
                requestPayloadSize = event.sizeBytes,
            )

            is WebSocketEvent.Disconnected -> repository.updateWsClose(
                sessionId = event.connectionId,
                responseDate = event.timestamp,
                tookMs = event.tookMs,
            )
        }
    }

    private fun wsHost(url: String) =
        url.removePrefix("wss://").removePrefix("ws://").substringBefore("/")

    private fun wsPath(url: String): String {
        val afterScheme = url.removePrefix("wss://").removePrefix("ws://")
        val afterHost = afterScheme.substringAfter("/", "")
        return if (afterHost.isEmpty()) "/" else "/$afterHost"
    }
}
