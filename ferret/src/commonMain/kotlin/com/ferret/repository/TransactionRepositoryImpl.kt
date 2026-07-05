package com.ferret.repository

import com.ferret.database.dao.TransactionDao
import com.ferret.database.entity.TransactionEntity
import com.ferret.database.mapper.toDomain
import com.ferret.database.mapper.toEntity
import com.ferret.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class TransactionRepositoryImpl(
    private val dao: TransactionDao
) : TransactionRepository {

    override suspend fun insert(transaction: Transaction): Long =
        dao.insert(transaction.toEntity())

    override suspend fun update(transaction: Transaction) =
        dao.update(transaction.toEntity())

    override suspend fun updateResponse(
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
    ) = dao.updateResponse(
        sessionId = sessionId,
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
        sessionId: String,
        responseDate: Long,
        tookMs: Long,
        error: String?,
    ) = dao.updateError(
        sessionId = sessionId,
        responseDate = responseDate,
        tookMs = tookMs,
        error = error,
    )

    override suspend fun delete(transaction: Transaction) =
        dao.delete(transaction.toEntity())

    override suspend fun clear() =
        dao.clear()

    override suspend fun get(id: Long): Transaction? =
        dao.getById(id)?.toDomain()

    override suspend fun getAll(): List<Transaction> =
        dao.getAll().map(TransactionEntity::toDomain)

    override fun observeAll(): Flow<List<Transaction>> =
        dao.observeAll().map { list -> list.map(TransactionEntity::toDomain) }

    override suspend fun deleteOlderThan(timestamp: Long) =
        dao.deleteOlderThan(timestamp)
}
