package com.ferret.repository

import com.ferret.database.dao.NetworkRecordDao
import com.ferret.database.entity.NetworkRecordEntity
import com.ferret.database.mapper.toDomain
import com.ferret.database.mapper.toEntity
import com.ferret.model.NetworkRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

internal class NetworkRecordRepositoryImpl(
    private val dao: NetworkRecordDao,
    private val retentionDurationMs: Long = DEFAULT_RETENTION_MS,
) : NetworkRecordRepository {

    private fun cutoffTimestamp(): Long =
        kotlin.time.Clock.System.now().toEpochMilliseconds() - retentionDurationMs

    override suspend fun insert(networkRecord: NetworkRecord): Long =
        dao.insert(networkRecord.toEntity())

    override suspend fun update(networkRecord: NetworkRecord) =
        dao.update(networkRecord.toEntity())

    override suspend fun updateResponse(
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
    ) = dao.updateResponse(
        id = id,
        responseDate = responseDate,
        tookMs = tookMs,
        responseCode = responseCode,
        responseMessage = responseMessage,
        responsePayloadSize = responsePayloadSize,
        responseContentType = responseContentType,
        responseHeaders = responseHeaders,
        responseHeadersSize = responseHeadersSize,
        responseBody = responseBody,
        responseTlsVersion = responseTlsVersion,
        responseCipherSuite = responseCipherSuite,
    )

    override suspend fun updateError(
        id: Long,
        responseDate: Long,
        tookMs: Long,
        error: String?,
    ) = dao.updateError(
        id = id,
        responseDate = responseDate,
        tookMs = tookMs,
        error = error,
    )

    override suspend fun delete(networkRecord: NetworkRecord) =
        dao.delete(networkRecord.toEntity())

    override suspend fun clear() =
        dao.clear()

    override suspend fun getById(id: Long): NetworkRecord? =
        dao.getById(id)?.toDomain()

    override suspend fun getAll(): List<NetworkRecord> {
        dao.deleteOlderThan(cutoffTimestamp())
        return dao.getAll().map(NetworkRecordEntity::toDomain)
    }

    override fun observeAll(): Flow<List<NetworkRecord>> =
        dao.observeAll()
            .onStart { dao.deleteOlderThan(cutoffTimestamp()) }
            .map { list -> list.map(NetworkRecordEntity::toDomain) }

    override suspend fun deleteOlderThan(timestamp: Long) =
        dao.deleteOlderThan(timestamp)

    companion object {
        private const val DEFAULT_RETENTION_MS = 12L * 60 * 60 * 1000
    }
}
