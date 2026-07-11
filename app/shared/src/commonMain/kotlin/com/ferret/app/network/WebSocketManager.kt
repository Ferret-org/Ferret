package com.ferret.app.network

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WebSocketManager(private val client: HttpClient) {

    sealed class State {
        object Idle : State()
        object Connecting : State()
        object Connected : State()
        object Disconnected : State()
        data class Error(val message: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var session: DefaultWebSocketSession? = null
    private var connectJob: Job? = null
    private var shouldReconnect = false

    fun connect(url: String) {
        shouldReconnect = true
        connectJob?.cancel()
        connectJob = scope.launch { doConnect(url) }
    }

    private suspend fun doConnect(url: String) {
        _state.value = State.Connecting
        try {
            client.webSocket(urlString = url) {
                session = this
                _state.value = State.Connected
                Logger.e("@@@@@") {
                    "connected"
                }
                try {
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                Logger.e("@@@@@") {
                                    frame.readText().toString()
                                }
                                _messages.emit(frame.readText())
                            }

                            else -> {}
                        }
                    }
                } finally {
                    session = null
                }
            }
            // Server closed cleanly — reconnect if still desired
            if (shouldReconnect) {
                _state.value = State.Disconnected
                delay(RECONNECT_DELAY_MS)
                doConnect(url)
            } else {
                _state.value = State.Idle
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            session = null
            if (shouldReconnect) {
                _state.value = State.Error(e.message ?: "Connection failed")
                delay(RECONNECT_DELAY_MS)
                doConnect(url)
            } else {
                _state.value = State.Disconnected
            }
        }
    }

    suspend fun send(text: String) {
        session?.send(Frame.Text(text))
    }

    fun disconnect() {
        shouldReconnect = false
        connectJob?.cancel()
        session = null
        _state.value = State.Idle
    }

    fun close() = scope.cancel()

    companion object {
        private const val RECONNECT_DELAY_MS = 3_000L
    }
}
