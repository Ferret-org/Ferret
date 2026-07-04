package com.ferret.database.mapper

import com.ferret.database.entity.TransactionEntity
import com.ferret.model.Transaction

internal fun Transaction.toEntity() = TransactionEntity(
    id = id,
    sessionId = sessionId,
    protocol = protocol,
    state = state,
    url = url,
    method = method,
    requestHeaders = requestHeaders,
    responseHeaders = responseHeaders,
    requestBody = requestBody,
    responseBody = responseBody,
    statusCode = statusCode,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp,
    durationMs = durationMs,
    isSecure = isSecure,
    errorMessage = errorMessage
)

internal fun TransactionEntity.toDomain() = Transaction(
    id = id,
    sessionId = sessionId,
    protocol = protocol,
    state = state,
    url = url,
    method = method,
    requestHeaders = requestHeaders,
    responseHeaders = responseHeaders,
    requestBody = requestBody,
    responseBody = responseBody,
    statusCode = statusCode,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp,
    durationMs = durationMs,
    isSecure = isSecure,
    errorMessage = errorMessage
)