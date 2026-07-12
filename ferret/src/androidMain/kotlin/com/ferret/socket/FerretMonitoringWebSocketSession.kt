package com.ferret.socket

import android.util.Base64
import android.util.Log
import com.ferret.di.FerretKoin
import com.ferret.model.WebSocketEvent
import com.ferret.usecase.SaveWebSocketEventUseCase
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

@OptIn(ExperimentalCoroutinesApi::class)
internal class FerretMonitoringWebSocketSession(
    private val delegate: DefaultClientWebSocketSession,
    private val url: String,
) : DefaultWebSocketSession by delegate {

    private val connectionId: String = UUID.randomUUID().toString()
    private val frameCounter = AtomicInteger(0)

    private val useCase: SaveWebSocketEventUseCase?
        get() = runCatching { FerretKoin.koin.get<SaveWebSocketEventUseCase>() }.getOrNull()

    // Proxy outgoing: user → _outgoing → observe → delegate.outgoing
    private val _outgoing: Channel<Frame> = Channel(Channel.UNLIMITED)

    // Proxy incoming: delegate.incoming → observe → _incoming → user
    private val _incoming: ReceiveChannel<Frame>

    override val outgoing: SendChannel<Frame> get() = _outgoing
    override val incoming: ReceiveChannel<Frame> get() = _incoming

    override suspend fun send(frame: Frame) = _outgoing.send(frame)

    init {
        // Fire Connected event
        launch {
            useCase?.save(WebSocketEvent.Connected(connectionId, System.currentTimeMillis(), url))
            Log.d("FerretWS", "WS CONNECTED $url")
        }

        // Outgoing proxy
        launch {
            for (frame in _outgoing) {
                if (frame !is Frame.Close) {
                    val count = frameCounter.incrementAndGet()
                    launch {
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
                try {
                    delegate.outgoing.send(frame)
                } catch (_: Exception) {
                    break
                }
            }
        }

        // Incoming proxy
        _incoming = produce(capacity = Channel.UNLIMITED) {
            try {
                for (frame in delegate.incoming) {
                    if (frame !is Frame.Close) {
                        val count = frameCounter.incrementAndGet()
                        launch {
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
                    send(frame)
                }
            } finally {
                launch {
                    useCase?.save(
                        WebSocketEvent.Disconnected(
                            connectionId,
                            System.currentTimeMillis()
                        )
                    )
                    Log.d("FerretWS", "WS DISCONNECTED $url")
                }
            }
        }
    }

    private fun Frame.toReadableString(): String = when (this) {
        is Frame.Text -> data.decodeToString().take(MAX_PAYLOAD_BYTES)
        else -> Base64.encodeToString(
            data.copyOfRange(0, minOf(data.size, MAX_PAYLOAD_BYTES)),
            Base64.NO_WRAP,
        )
    }

    companion object {
        private const val MAX_PAYLOAD_BYTES = 65_536
    }
}