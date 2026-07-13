package com.ferret.common

enum class FerretTab(val title: String) {
    ALL("ALL"),
    HTTP("HTTP"),
    WEBSOCKET("WebSockets")
}

enum class FerretDetailTab(
    val title: String,
) {
    OVERVIEW("Overview"),
    REQUEST("Request"),
    RESPONSE("Response"),
    TIMING("Timing"),
}

enum class FerretWebSocketDetailTab(
    val title: String,
) {
    OVERVIEW("Overview"),
    MESSAGES("Messages"),
    HEADERS("Headers"),
    TIMING("Timing"),
}