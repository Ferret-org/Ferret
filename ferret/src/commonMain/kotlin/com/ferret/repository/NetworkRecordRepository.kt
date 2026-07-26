package com.ferret.repository

import com.ferret.model.NetworkRecord
import kotlinx.coroutines.flow.Flow

interface NetworkRecordRepository {

    suspend fun insert(networkRecord: NetworkRecord): Long

    suspend fun update(networkRecord: NetworkRecord)

    suspend fun updateResponse(
        id: Long,
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
        id: Long,
        responseDate: Long,
        tookMs: Long,
        error: String?,
    )

    suspend fun delete(networkRecord: NetworkRecord)

    suspend fun clear()

    suspend fun getById(id: Long): NetworkRecord?

    suspend fun getAll(): List<NetworkRecord>

    fun observeAll(): Flow<List<NetworkRecord>>

    suspend fun deleteOlderThan(timestamp: Long)
}
