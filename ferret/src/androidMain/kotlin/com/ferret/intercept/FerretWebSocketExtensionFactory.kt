package com.ferret.intercept

import io.ktor.util.AttributeKey
import io.ktor.websocket.WebSocketExtensionFactory
import java.util.concurrent.ConcurrentLinkedQueue

internal object FerretWebSocketExtensionFactory : WebSocketExtensionFactory<Unit, FerretWebSocketExtension> {

    override val key: AttributeKey<FerretWebSocketExtension> =
        AttributeKey("FerretWebSocketExtension")
    override val rsv1: Boolean = false
    override val rsv2: Boolean = false
    override val rsv3: Boolean = false

    // Populated by HttpSend interceptor before the WebSocket upgrade; polled by install()
    // which runs in the same coroutine immediately after. FIFO ordering is correct for
    // sequential connections; for rare parallel connections this is best-effort.
    private val pendingUrls = ConcurrentLinkedQueue<String>()

    internal fun enqueueUrl(url: String) {
        pendingUrls.offer(url)
    }

    override fun install(config: Unit.() -> Unit): FerretWebSocketExtension {
        return FerretWebSocketExtension(url = pendingUrls.poll() ?: "unknown")
    }
}
