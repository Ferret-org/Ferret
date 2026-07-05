package com.ferret.intercept

import android.content.Context
import android.util.Log
import com.ferret.AndroidContextHolder
import com.ferret.FerretConfiguration
import com.ferret.FerretSdk
import com.ferret.model.Header
import com.ferret.model.Transaction
import com.ferret.notification.NotificationKit
import com.ferret.usecase.InitializeFerretUseCase
import com.ferret.usecase.SaveTransactionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
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

    private val useCase: SaveTransactionUseCase?
        get() = FerretSdk.transactionRepository?.let(::SaveTransactionUseCase)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val sessionId = UUID.randomUUID().toString()
        val startTime = System.currentTimeMillis()

        saveRequest(sessionId, startTime, request)

        return try {
            val response = chain.proceed(request)
            saveResponse(sessionId, startTime, request, response)
            response
        } catch (e: Exception) {
            saveError(sessionId, startTime, e)
            throw e
        }
    }

    private fun saveRequest(sessionId: String, startTime: Long, request: Request) {
        val uc = useCase ?: return
        val requestHeaders = request.headers.map { (name, value) -> Header(name, value) }
        val body = request.body

        NotificationKit.push {
            title(request.method)
            message(request.url.encodedPath)
        }

        scope.launch {
            uc.saveRequest(
                Transaction(
                    sessionId = sessionId,
                    requestDate = startTime,
                    protocol = request.url.scheme.uppercase(),
                    method = request.method,
                    url = request.url.toString(),
                    host = request.url.host,
                    path = request.url.encodedPath,
                    scheme = request.url.scheme,
                    requestPayloadSize = body?.contentLength()?.takeIf { it >= 0 } ?: 0,
                    requestContentType = body?.contentType()?.toString(),
                    requestHeaders = requestHeaders,
                    requestHeadersSize = requestHeaders.size,
                )
            )
            Log.d("Ferret", "→ ${request.method} ${request.url}")
        }
    }

    private fun saveResponse(
        sessionId: String,
        startTime: Long,
        request: Request,
        response: Response
    ) {
        val uc = useCase ?: return
        val endTime = System.currentTimeMillis()
        val responseHeaders = response.headers.map { (name, value) -> Header(name, value) }
        val bufferedBody: ResponseBody = response.peekBody(Long.MAX_VALUE)
        val tlsHandshake = response.handshake

        NotificationKit.push {
            title(request.method)
            message(bufferedBody.string())
        }

        scope.launch {
            uc.saveResponse(
                sessionId = sessionId,
                responseDate = endTime,
                tookMs = endTime - startTime,
                responseCode = response.code,
                responseMessage = response.message,
                responsePayloadSize = bufferedBody.contentLength().takeIf { it >= 0 } ?: 0,
                responseContentType = bufferedBody.contentType()?.toString(),
                responseHeaders = responseHeaders,
                responseBody = bufferedBody.string(),
                responseTlsVersion = tlsHandshake?.tlsVersion?.javaName,
                responseCipherSuite = tlsHandshake?.cipherSuite?.javaName,
            )
            Log.d("Ferret", "← ${response.code} ${request.url} (${endTime - startTime}ms)")
        }
    }

    private fun saveError(sessionId: String, startTime: Long, e: Exception) {
        val uc = useCase ?: return
        val endTime = System.currentTimeMillis()

        scope.launch {
            uc.saveError(
                sessionId = sessionId,
                responseDate = endTime,
                tookMs = endTime - startTime,
                error = e.message,
            )
            Log.e("Ferret", "✗ $sessionId ${e.message}")
        }
    }
}
