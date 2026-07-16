package com.ferret.common

enum class FerretTab(val title: String) {
    ALL("ALL"),
    HTTP("HTTP"),
    WEBSOCKET("WebSockets")
}

sealed interface FerretNetworkDetailTab {

    val title: String

    enum class Http(
        override val title: String,
    ) : FerretNetworkDetailTab {
        OVERVIEW("Overview"),
        REQUEST("Request"),
        RESPONSE("Response"),
        TIMING("Timing"),
    }

    enum class WebSocket(
        override val title: String,
    ) : FerretNetworkDetailTab {
        OVERVIEW("Overview"),
        REQUEST("Request"),
        RESPONSE("Response"),
    }
}