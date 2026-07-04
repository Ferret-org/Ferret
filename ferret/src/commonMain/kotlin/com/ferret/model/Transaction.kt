package com.ferret.model

data class Transaction(

    val id: Long = 0,

    val sessionId: String,

    val protocol: TransactionProtocol,

    val state: TransactionState,

    val url: String,

    val method: HttpMethod?,

    val requestHeaders: List<Header> = emptyList(),

    val responseHeaders: List<Header> = emptyList(),

    val requestBody: Body? = null,

    val responseBody: Body? = null,

    val statusCode: Int? = null,

    val startTimestamp: Long,

    val endTimestamp: Long? = null,

    val durationMs: Long? = null,

    val isSecure: Boolean,

    val errorMessage: String? = null
)