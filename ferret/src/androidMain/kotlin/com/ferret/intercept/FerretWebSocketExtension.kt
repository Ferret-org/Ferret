package com.ferret.intercept

import android.util.Base64
import android.util.Log
import com.ferret.FerretSdk
import com.ferret.model.WebSocketEvent
import com.ferret.usecase.SaveWebSocketEventUseCase
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketExtension
import io.ktor.websocket.WebSocketExtensionFactory
import io.ktor.websocket.WebSocketExtensionHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

internal class FerretWebSocketExtension(
    private val url: String,
) : WebSocketExtension<Unit> {

    override val protocols: List<WebSocketExtensionHeader> = emptyList()

    @Suppress("UNCHECKED_CAST")
    override val factory: WebSocketExtensionFactory<Unit, out WebSocketExtension<Unit>> =
        FerretWebSocketExtensionFactory as WebSocketExtensionFactory<Unit, out WebSocketExtension<Unit>>

    private val connectionId = java.util.UUID.randomUUID().toString()
    private val scope = CoroutineScope(Dispatchers.IO)
    private val frameCounter = AtomicInteger(0)

    private val useCase: SaveWebSocketEventUseCase?
        get() = FerretSdk.transactionRepository?.let(::SaveWebSocketEventUseCase)

    override fun clientNegotiation(negotiatedProtocols: List<WebSocketExtensionHeader>): Boolean {
        scope.launch {
            useCase?.save(WebSocketEvent.Connected(connectionId, System.currentTimeMillis(), url))
            Log.d("Ferret", "WS ↑ CONNECTED $url")
        }
        return true
    }

    override fun serverNegotiation(
        requestedProtocols: List<WebSocketExtensionHeader>,
    ): List<WebSocketExtensionHeader> = emptyList()

    override fun processIncomingFrame(frame: Frame): Frame {
        if (frame is Frame.Close) {
            scope.launch {
                useCase?.save(WebSocketEvent.Disconnected(connectionId, System.currentTimeMillis()))
                Log.d("Ferret", "WS ↓ DISCONNECTED $url")
            }
        } else {
            val count = frameCounter.incrementAndGet()
            scope.launch {
                useCase?.save(
                    WebSocketEvent.FrameReceived(
                        connectionId = connectionId,
                        timestamp = System.currentTimeMillis(),
                        frameType = frame.frameType.name,
                        data = frame.toReadableString(),
                        sizeBytes = frame.data.size.toLong(),
                        count = count,
                    )
                )
            }
        }
        return frame
    }

    override fun processOutgoingFrame(frame: Frame): Frame {
        if (frame !is Frame.Close) {
            val count = frameCounter.incrementAndGet()
            scope.launch {
                useCase?.save(
                    WebSocketEvent.FrameSent(
                        connectionId = connectionId,
                        timestamp = System.currentTimeMillis(),
                        frameType = frame.frameType.name,
                        data = frame.toReadableString(),
                        sizeBytes = frame.data.size.toLong(),
                        count = count,
                    )
                )
            }
        }
        return frame
    }

    private fun Frame.toReadableString(): String = when (this) {
        is Frame.Text -> data.decodeToString().take(MAX_PAYLOAD_BYTES)
        else -> Base64.encodeToString(
            data.copyOfRange(0, minOf(data.size, MAX_PAYLOAD_BYTES)),
            Base64.NO_WRAP,
        )
    }

    private companion object {
        const val MAX_PAYLOAD_BYTES = 65_536
    }
}
