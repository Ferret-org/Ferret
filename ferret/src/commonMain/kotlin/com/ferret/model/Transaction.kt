package com.ferret.model

data class NetworkRecord(

    val id: Long = 0,

    val sessionId: String,

    val requestDate: Long,

    val responseDate: Long? = null,

    val tookMs: Long? = null,

    val protocol: String,

    val method: String?,

    val url: String,

    val host: String,

    val path: String,

    val scheme: String,

    val responseTlsVersion: String? = null,

    val responseCipherSuite: String? = null,

    val requestPayloadSize: Long = 0,

    val requestContentType: String? = null,

    val requestHeaders: List<Header> = emptyList(),

    val requestHeadersSize: Int = 0,

    val requestBody: String? = null,

    val isRequestBodyEncoded: Boolean = false,

    val responseCode: Int? = null,

    val responseMessage: String? = null,

    val error: String? = null,

    val responsePayloadSize: Long = 0,

    val responseContentType: String? = null,

    val responseHeaders: List<Header> = emptyList(),

    val responseHeadersSize: Int = 0,

    val responseBody: String? = null,

    val isResponseBodyEncoded: Boolean = false,
) {
    val isWebSocket: Boolean
        get() = protocol.equals(
            other = "WS",
            ignoreCase = true,
        )
}
