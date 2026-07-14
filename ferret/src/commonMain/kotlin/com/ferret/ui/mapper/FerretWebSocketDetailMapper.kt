package com.ferret.ui.mapper

import com.ferret.model.FerretDetailItem
import com.ferret.model.FerretDetailSection
import com.ferret.model.NetworkRecord
import com.ferret.utils.formatBytes

internal fun NetworkRecord.toWebSocketOverviewSections(): List<FerretDetailSection> =
    listOf(
        FerretDetailSection(
            title = "Overview",
            items = listOf(
                FerretDetailItem(
                    label = "URL",
                    value = url,
                ),
                FerretDetailItem(
                    label = "Request Size",
                    value = requestPayloadSize
                        .takeIf { it > 0 }
                        ?.formatBytes(),
                ),
                FerretDetailItem(
                    label = "Response Size",
                    value = responsePayloadSize
                        .takeIf { it > 0 }
                        ?.formatBytes(),
                ),
                FerretDetailItem(
                    label = "Error",
                    value = error,
                ),
            ),
        ),
    )