package com.ferret.ui.mapper

import com.ferret.model.FerretDetailItem
import com.ferret.model.FerretDetailSection
import com.ferret.model.NetworkRecord
import com.ferret.utils.formatBytes
import com.ferret.utils.formatTime

internal fun NetworkRecord.toHttpOverviewSections(): List<FerretDetailSection> {
    return listOf(
        FerretDetailSection(
            title = "General",
            items = listOf(
                FerretDetailItem(
                    label = "URL",
                    value = url,
                ),
                FerretDetailItem(
                    label = "Host",
                    value = host,
                ),
                FerretDetailItem(
                    label = "Path",
                    value = path,
                ),
                FerretDetailItem(
                    label = "Protocol",
                    value = protocol,
                ),
                FerretDetailItem(
                    label = "Method",
                    value = method.orEmpty(),
                ),
                FerretDetailItem(
                    label = "Started",
                    value = requestDate.formatTime(),
                ),
                FerretDetailItem(
                    label = "Finished",
                    value = responseDate?.formatTime(),
                ),
                FerretDetailItem(
                    label = "Duration",
                    value = tookMs?.let { "$it ms" },
                ),
                FerretDetailItem(
                    label = "TLS",
                    value = buildTlsInfo(this),
                ),
            ),
        ),
        FerretDetailSection(
            title = "Sizes",
            items = listOf(
                FerretDetailItem(
                    label = "Request Payload",
                    value = requestPayloadSize.formatBytes(),
                ),
                FerretDetailItem(
                    label = "Response Payload",
                    value = responsePayloadSize.formatBytes(),
                ),
                FerretDetailItem(
                    label = "Total Transfer",
                    value = (requestPayloadSize + responsePayloadSize).formatBytes(),
                ),
            ),
        ),
    )
}

private fun buildTlsInfo(
    network: NetworkRecord,
): String {
    val tlsVersion = network.responseTlsVersion
    val cipherSuite = network.responseCipherSuite

    return when {
        tlsVersion != null && cipherSuite != null -> {
            "$tlsVersion / $cipherSuite"
        }

        tlsVersion != null -> tlsVersion

        cipherSuite != null -> cipherSuite

        else -> "—"
    }
}