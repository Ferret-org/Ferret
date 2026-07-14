package com.ferret.intercept

import android.content.Context
import com.ferret.AndroidContextHolder
import com.ferret.FerretConfiguration
import com.ferret.FerretSdk
import com.ferret.model.Header
import com.ferret.model.NetworkRecord
import com.ferret.notification.NotificationKit
import com.ferret.socket.FerretMonitoringWebSocketSession
import com.ferret.usecase.InitializeFerretUseCase
import com.ferret.usecase.SaveTransactionUseCase
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.save
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.http.encodedPath
import io.ktor.util.AttributeKey
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.io.readByteArray
import java.util.UUID

object Ferret {
    class Config {
        lateinit var context: Context
        var configuration: FerretConfiguration = FerretConfiguration()
    }
}

fun HttpClientConfig<*>.install(ferret: Ferret, block: Ferret.Config.() -> Unit) {
    val config = Ferret.Config().apply(block)
    AndroidContextHolder.context = config.context.applicationContext
    InitializeFerretUseCase(config.configuration).execute()
    install(WebSockets)
    install(FerretMonitorPlugin)
}

private object FerretMonitorPlugin : HttpClientPlugin<Unit, FerretMonitorPlugin> {

    override val key: AttributeKey<FerretMonitorPlugin> = AttributeKey("FerretMonitor")

    val appSessionId: String = UUID.randomUUID().toString()

    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private const val MAX_BODY_BYTES = 65_536

    override fun prepare(block: Unit.() -> Unit): FerretMonitorPlugin = this

    @OptIn(InternalAPI::class)
    override fun install(plugin: FerretMonitorPlugin, scope: HttpClient) {

        scope.plugin(HttpSend).intercept { request ->
            if (request.headers[HttpHeaders.Upgrade]?.lowercase() == "websocket") {
                return@intercept execute(request)
            }

            val useCase = runCatching { FerretSdk.transactionRepository }
                .getOrNull()?.let(::SaveTransactionUseCase)
            val sessionId = appSessionId
            val startTime = System.currentTimeMillis()
            val requestHeaders = request.headers.build().entries()
                .flatMap { (key, values) -> values.map { Header(key, it) } }
            val requestBodySize = (request.body as? OutgoingContent.ByteArrayContent)
                ?.bytes()?.size?.toLong() ?: 0L

            NotificationKit.push {
                title(request.method.value)
                message(request.url.encodedPath)
            }

            ioScope.launch {
                useCase?.saveRequest(
                    NetworkRecord(
                        sessionId = sessionId,
                        requestDate = startTime,
                        protocol = request.url.protocol.name.uppercase(),
                        method = request.method.value,
                        url = request.url.buildString(),
                        host = request.url.host,
                        path = request.url.encodedPath,
                        scheme = request.url.protocol.name,
                        requestPayloadSize = requestBodySize,
                        requestContentType = request.headers[HttpHeaders.ContentType],
                        requestHeaders = requestHeaders,
                        requestHeadersSize = requestHeaders.size,
                    )
                )
            }

            try {
                val call = execute(request)
                val savedCall = call.save()
                val endTime = System.currentTimeMillis()
                val responseBodyText = savedCall.response.rawContent
                    .readRemaining().readByteArray()
                    .decodeToString().take(MAX_BODY_BYTES)
                val responseHeaders = savedCall.response.headers.entries()
                    .flatMap { (key, values) -> values.map { Header(key, it) } }

                NotificationKit.push {
                    title("${savedCall.response.status.value} ${request.method.value}")
                    message(request.url.encodedPath)
                }

                ioScope.launch {
                    useCase?.saveResponse(
                        sessionId = sessionId,
                        responseDate = endTime,
                        tookMs = endTime - startTime,
                        responseCode = savedCall.response.status.value,
                        responseMessage = savedCall.response.status.description,
                        responsePayloadSize = responseBodyText.length.toLong(),
                        responseContentType = savedCall.response.headers[HttpHeaders.ContentType],
                        responseHeaders = responseHeaders,
                        responseBody = responseBodyText,
                        responseTlsVersion = null,
                        responseCipherSuite = null,
                    )
                }

                savedCall
            } catch (e: Exception) {
                val endTime = System.currentTimeMillis()
                ioScope.launch {
                    useCase?.saveError(
                        sessionId = sessionId,
                        responseDate = endTime,
                        tookMs = endTime - startTime,
                        error = e.message,
                    )
                }
                throw e
            }
        }

        scope.responsePipeline.intercept(HttpResponsePipeline.After) { (info, body) ->
            if (body !is DefaultClientWebSocketSession) {
                proceed()
                return@intercept
            }
            val url = context.request.url.toString()
            val monitoringDelegate = FerretMonitoringWebSocketSession(body, url, appSessionId)
            val wrapped = DefaultClientWebSocketSession(body.call, monitoringDelegate)
            proceedWith(HttpResponseContainer(info, wrapped))
        }
    }
}
