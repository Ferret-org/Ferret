package com.ferret.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ferret.model.FerretDetailSection

@Composable
internal fun FerretDetailSectionCard(
    section: FerretDetailSection,
    modifier: Modifier = Modifier,
) {
    FerretCard(
        modifier = modifier.fillMaxWidth(),
        elevation = 3.dp,
    ) {
        FerretSectionTitle(
            title = section.title,
        )

        section.items.forEach { item ->
            item.value
                ?.takeIf { value ->
                    value.isNotBlank()
                }
                ?.let { value ->
                    FerretKeyValueRow(
                        key = item.label,
                        value = value,
                    )
                }
        }
    }
}