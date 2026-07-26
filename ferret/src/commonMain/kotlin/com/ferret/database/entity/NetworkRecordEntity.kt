package com.ferret.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ferret.database.DatabaseConstants
import com.ferret.model.Header

@Entity(
    tableName = DatabaseConstants.NETWORK_RECORD_TABLE,
    indices = [
        Index("sessionId"),
        Index("requestDate"),
        Index("responseCode"),
        Index("protocol")
    ]
)
data class NetworkRecordEntity(

    @PrimaryKey(autoGenerate = true)
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
)
