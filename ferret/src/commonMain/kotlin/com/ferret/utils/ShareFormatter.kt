package com.ferret.utils

import com.ferret.model.NetworkRecord

fun NetworkRecord.toCurlCommand(): String {
    val sb = StringBuilder()
    sb.append("curl -X ${method ?: "GET"} '${url}'")

    requestHeaders.forEach { header ->
        sb.append(" \\\n  -H '${header.name}: ${header.value}'")
    }

    val body = requestBody?.takeIf { it.isNotBlank() }
    if (body != null) {
        val escaped = body.replace("'", "'\\''")
        sb.append(" \\\n  --data-raw '$escaped'")
    }

    return sb.toString()
}

fun NetworkRecord.toShareText(): String {
    val sb = StringBuilder()

    // General
    sb.appendLine("=== General ===")
    sb.appendLine("Method : ${method ?: "-"}")
    sb.appendLine("URL    : $url")
    val status = if (responseCode != null) {
        "$responseCode${if (responseMessage != null) " $responseMessage" else ""}"
    } else {
        error ?: "Pending"
    }
    sb.appendLine("Status : $status")
    if (tookMs != null) sb.appendLine("Time   : ${tookMs}ms")

    // Request headers
    if (requestHeaders.isNotEmpty()) {
        sb.appendLine()
        sb.appendLine("=== Request Headers ===")
        requestHeaders.forEach { sb.appendLine("${it.name}: ${it.value}") }
    }

    // Request body
    val reqBody = requestBody?.takeIf { it.isNotBlank() }
    if (reqBody != null) {
        sb.appendLine()
        sb.appendLine("=== Request Body ===")
        sb.appendLine(formatBody(reqBody, requestContentType))
    }

    // Response headers
    if (responseHeaders.isNotEmpty()) {
        sb.appendLine()
        sb.appendLine("=== Response Headers ===")
        responseHeaders.forEach { sb.appendLine("${it.name}: ${it.value}") }
    }

    // Response body
    val resBody = responseBody?.takeIf { it.isNotBlank() }
    if (resBody != null) {
        sb.appendLine()
        sb.appendLine("=== Response Body ===")
        sb.appendLine(formatBody(resBody, responseContentType))
    }

    return sb.toString().trimEnd()
}