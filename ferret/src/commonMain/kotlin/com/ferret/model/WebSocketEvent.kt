package com.ferret.model

sealed class WebSocketEvent {
    abstract val connectionId: String
    abstract val timestamp: Long

    data class Connected(
        override val connectionId: String,
        override val timestamp: Long,
        val url: String,
    ) : WebSocketEvent()

    data class FrameReceived(
        override val connectionId: String,
        override val timestamp: Long,
        val frameType: String,
        val data: String,
        val sizeBytes: Long,
        val count: Int,
    ) : WebSocketEvent()

    data class FrameSent(
        override val connectionId: String,
        override val timestamp: Long,
        val frameType: String,
        val data: String,
        val sizeBytes: Long,
        val count: Int,
    ) : WebSocketEvent()

    data class Disconnected(
        override val connectionId: String,
        override val timestamp: Long,
    ) : WebSocketEvent()
}
