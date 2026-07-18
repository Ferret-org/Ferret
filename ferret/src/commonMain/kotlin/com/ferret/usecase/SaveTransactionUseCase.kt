package com.ferret.usecase

import com.ferret.database.converter.TransactionConverters
import com.ferret.model.Header
import com.ferret.model.NetworkRecord
import com.ferret.repository.TransactionRepository

internal class SaveTransactionUseCase(
    private val repository: TransactionRepository
) {

    suspend fun saveRequest(transaction: NetworkRecord): Long =
        repository.insert(transaction)

    suspend fun saveResponse(
        id: Long,
        responseDate: Long,
        tookMs: Long,
        responseCode: Int,
        responseMessage: String,
        responsePayloadSize: Long,
        responseContentType: String?,
        responseHeaders: List<Header>,
        responseBody: String?,
        responseTlsVersion: String?,
        responseCipherSuite: String?,
    ) {
        repository.updateResponse(
            id = id,
            responseDate = responseDate,
            tookMs = tookMs,
            responseCode = responseCode,
            responseMessage = responseMessage,
            responsePayloadSize = responsePayloadSize,
            responseContentType = responseContentType,
            responseHeaders = TransactionConverters.json.encodeToString(responseHeaders),
            responseHeadersSize = responseHeaders.size,
            responseBody = responseBody,
            responseTlsVersion = responseTlsVersion,
            responseCipherSuite = responseCipherSuite,
        )
    }

    suspend fun saveError(
        id: Long,
        responseDate: Long,
        tookMs: Long,
        error: String?,
    ) {
        repository.updateError(
            id = id,
            responseDate = responseDate,
            tookMs = tookMs,
            error = error,
        )
    }
}
