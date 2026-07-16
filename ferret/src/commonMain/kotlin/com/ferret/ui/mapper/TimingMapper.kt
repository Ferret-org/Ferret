package com.ferret.ui.mapper

import com.ferret.model.FerretDetailItem
import com.ferret.model.FerretDetailSection
import com.ferret.model.NetworkRecord
import com.ferret.utils.formatTime

internal fun NetworkRecord.toTimingSections(): List<FerretDetailSection> =
    listOf(
        FerretDetailSection(
            title = "Timing",
            items = listOf(
                FerretDetailItem(
                    label = "Request Started",
                    value = requestDate.formatTime(),
                ),
                FerretDetailItem(
                    label = "Response Finished",
                    value = responseDate?.formatTime(),
                ),
                FerretDetailItem(
                    label = "Total Duration",
                    value = tookMs
                        ?.let { duration ->
                            "$duration ms"
                        }
                ),
            ),
        ),
    )