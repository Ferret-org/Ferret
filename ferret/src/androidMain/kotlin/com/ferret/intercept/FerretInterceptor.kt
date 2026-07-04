package com.ferret.intercept

import android.content.Context
import com.ferret.AndroidContextHolder
import com.ferret.FerretConfiguration
import com.ferret.FerretSdk
import com.ferret.model.Body
import com.ferret.model.Header
import com.ferret.model.HttpMethod
import com.ferret.model.Transaction
import com.ferret.model.TransactionProtocol
import com.ferret.model.TransactionState
import com.ferret.notification.NotificationKit
import com.ferret.usecase.InitializeFerretUseCase
import com.ferret.usecase.SaveTransactionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID

class FerretInterceptor(
    context: Context,
    configuration: FerretConfiguration = FerretConfiguration()
) : Interceptor {

    init {
        AndroidContextHolder.context = context.applicationContext
        InitializeFerretUseCase(configuration).execute()
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    private val saveTransaction: SaveTransactionUseCase?
        get() = FerretSdk.transactionRepository?.let(::SaveTransactionUseCase)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val sessionId = UUID.randomUUID().toString()
        val startTime = System.currentTimeMillis()

        NotificationKit.push {
            title("Send")
            message(request.url.toString())
        }

        val requestHeaders = request.headers.map { (name, value) -> Header(name, value) }
        val requestBody = request.body?.let { body ->
            Body(
                contentType = body.contentType()?.toString(),
                sizeInBytes = body.contentLength().takeIf { it >= 0 } ?: 0L
            )
        }

        scope.launch {
            saveTransaction?.invoke(
                Transaction(
                    sessionId = sessionId,
                    protocol = TransactionProtocol.HTTP,
                    state = TransactionState.STARTED,
                    url = request.url.toString(),
                    method = runCatching { HttpMethod.valueOf(request.method) }.getOrNull(),
                    requestHeaders = requestHeaders,
                    requestBody = requestBody,
                    startTimestamp = startTime,
                    isSecure = request.isHttps
                )
            )
        }

        return try {
            val response = chain.proceed(request)
            val endTime = System.currentTimeMillis()
            val responseBodyContent = response.peekBody(Long.MAX_VALUE)

            NotificationKit.push {
                title("Receive")
                message(responseBodyContent.toString())
            }

            scope.launch {
                saveTransaction?.update(
                    Transaction(
                        sessionId = sessionId,
                        protocol = TransactionProtocol.HTTP,
                        state = TransactionState.COMPLETED,
                        url = request.url.toString(),
                        method = runCatching { HttpMethod.valueOf(request.method) }.getOrNull(),
                        requestHeaders = requestHeaders,
                        requestBody = requestBody,
                        responseHeaders = response.headers.map { (name, value) -> Header(name, value) },
                        responseBody = Body(
                            contentType = responseBodyContent.contentType()?.toString(),
                            content = responseBodyContent.string(),
                            sizeInBytes = responseBodyContent.contentLength().takeIf { it >= 0 } ?: 0L
                        ),
                        statusCode = response.code,
                        startTimestamp = startTime,
                        endTimestamp = endTime,
                        durationMs = endTime - startTime,
                        isSecure = request.isHttps
                    )
                )
            }

            response
        } catch (e: Exception) {
            val endTime = System.currentTimeMillis()
            scope.launch {
                saveTransaction?.update(
                    Transaction(
                        sessionId = sessionId,
                        protocol = TransactionProtocol.HTTP,
                        state = TransactionState.FAILED,
                        url = request.url.toString(),
                        method = runCatching { HttpMethod.valueOf(request.method) }.getOrNull(),
                        requestHeaders = requestHeaders,
                        requestBody = requestBody,
                        startTimestamp = startTime,
                        endTimestamp = endTime,
                        durationMs = endTime - startTime,
                        isSecure = request.isHttps,
                        errorMessage = e.message
                    )
                )
            }
            throw e
        }
    }
}
