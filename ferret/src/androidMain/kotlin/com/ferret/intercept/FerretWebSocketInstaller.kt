package com.ferret.intercept

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.util.AttributeKey

fun HttpClientConfig<*>.installFerretWebSocket() {
    install(WebSockets)
    install(FerretWsMonitorPlugin)
}

private object FerretWsMonitorPlugin : HttpClientPlugin<Unit, FerretWsMonitorPlugin> {
    override val key: AttributeKey<FerretWsMonitorPlugin> = AttributeKey("FerretWsMonitor")

    override fun prepare(block: Unit.() -> Unit): FerretWsMonitorPlugin = this

    override fun install(plugin: FerretWsMonitorPlugin, scope: HttpClient) {
        scope.responsePipeline.intercept(HttpResponsePipeline.After) { (info, body) ->
            if (body !is DefaultClientWebSocketSession) {
                proceed()
                return@intercept
            }
            val url = context.request.url.toString()
            val monitoringDelegate = FerretMonitoringWebSocketSession(body, url)
            val wrapped = DefaultClientWebSocketSession(body.call, monitoringDelegate)
            proceedWith(HttpResponseContainer(info, wrapped))
        }
    }
}
