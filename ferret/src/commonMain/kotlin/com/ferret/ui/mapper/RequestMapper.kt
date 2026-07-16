package com.ferret.ui.mapper

import com.ferret.model.FerretDetailItem
import com.ferret.model.FerretDetailSection
import com.ferret.model.NetworkRecord
import com.ferret.utils.formatBytes

internal fun NetworkRecord.toRequestSections(): List<FerretDetailSection> =
    listOf(
        FerretDetailSection(
            title = "Request",
            items = listOf(
                FerretDetailItem(
                    label = "Method",
                    value = method.orEmpty(),
                ),
                FerretDetailItem(
                    label = "URL",
                    value = url,
                ),
                FerretDetailItem(
                    label = "Content Type",
                    value = requestContentType,
                ),
                FerretDetailItem(
                    label = "Payload Size",
                    value = requestPayloadSize.formatBytes(),
                ),
                FerretDetailItem(
                    label = "Headers Size",
                    value = requestHeadersSize
                        .toLong()
                        .formatBytes(),
                ),
                FerretDetailItem(
                    label = "Encoded",
                    value = isRequestBodyEncoded.toDisplayText(),
                ),
            ),
        ),
    )

internal fun Boolean.toDisplayText(): String =
    if (this) "Yes" else "No"