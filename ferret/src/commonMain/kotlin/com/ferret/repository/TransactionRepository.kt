package com.ferret.repository

import com.ferret.model.NetworkRecord
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun insert(transaction: NetworkRecord): Long

    suspend fun update(transaction: NetworkRecord)

    suspend fun updateResponse(
        sessionId: String,
        responseDate: Long,
        tookMs: Long,
        responseCode: Int,
        responseMessage: String,
        responsePayloadSize: Long,
        responseContentType: String?,
        responseHeaders: String,
        responseHeadersSize: Int,
        responseBody: String?,
        responseTlsVersion: String?,
        responseCipherSuite: String?,
    )

    suspend fun updateError(
        sessionId: String,
        responseDate: Long,
        tookMs: Long,
        error: String?,
    )

    suspend fun updateWsFrameIn(sessionId: String, responseBody: String?, responsePayloadSize: Long)

    suspend fun updateWsFrameOut(sessionId: String, requestBody: String?, requestPayloadSize: Long)

    suspend fun updateWsClose(sessionId: String, responseDate: Long, tookMs: Long)

    suspend fun delete(transaction: NetworkRecord)

    suspend fun clear()

    suspend fun getById(id: Long): NetworkRecord?

    suspend fun getAll(): List<NetworkRecord>

    fun observeAll(): Flow<List<NetworkRecord>>

    suspend fun deleteOlderThan(timestamp: Long)
}
