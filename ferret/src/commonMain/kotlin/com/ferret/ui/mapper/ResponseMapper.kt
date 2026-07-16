package com.ferret.ui.mapper

import com.ferret.model.FerretDetailItem
import com.ferret.model.FerretDetailSection
import com.ferret.model.NetworkRecord
import com.ferret.utils.formatBytes

internal fun NetworkRecord.toResponseSections(): List<FerretDetailSection> =
    listOf(
        FerretDetailSection(
            title = "Response",
            items = buildList {
                add(
                    FerretDetailItem(
                        label = "Status",
                        value = responseStatus(),
                    )
                )

                add(
                    FerretDetailItem(
                        label = "Content Type",
                        value = responseContentType,
                    )
                )

                add(
                    FerretDetailItem(
                        label = "Payload Size",
                        value = responsePayloadSize.formatBytes(),
                    )
                )

                add(
                    FerretDetailItem(
                        label = "Headers Size",
                        value = responseHeadersSize
                            .toLong()
                            .formatBytes(),
                    )
                )

                add(
                    FerretDetailItem(
                        label = "Encoded",
                        value = isResponseBodyEncoded.toDisplayText(),
                    )
                )

                error?.let { error ->
                    add(
                        FerretDetailItem(
                            label = "Error",
                            value = error,
                        )
                    )
                }
            },
        ),
    )

private fun NetworkRecord.responseStatus(): String? {
    if (responseCode == null && responseMessage == null) {
        return null
    }

    return buildString {
        responseCode?.let { code ->
            append(code)
        }

        responseMessage?.let { message ->
            if (isNotEmpty()) {
                append(" ")
            }

            append(message)
        }
    }
}