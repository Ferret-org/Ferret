package com.ferret.repository

import com.ferret.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun insert(transaction: Transaction): Long

    suspend fun update(transaction: Transaction)

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

    suspend fun delete(transaction: Transaction)

    suspend fun clear()

    suspend fun get(id: Long): Transaction?

    suspend fun getAll(): List<Transaction>

    fun observeAll(): Flow<List<Transaction>>

    suspend fun deleteOlderThan(timestamp: Long)
}
