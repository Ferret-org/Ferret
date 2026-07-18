package com.ferret.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ferret.model.FerretDetailSection
import com.ferret.ui.theme.FerretTypography
import com.ferret.utils.copyToClipboard

@Composable
internal fun FerretDetailSectionCard(
    section: FerretDetailSection,
    modifier: Modifier = Modifier,
) {
    val sectionText = remember(section) {
        section.items
            .filter { !it.value.isNullOrBlank() }
            .joinToString("\n") { "${it.label}: ${it.value}" }
    }

    FerretCard(
        modifier = modifier.fillMaxWidth(),
        elevation = 3.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = section.title,
                style = FerretTypography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = { copyToClipboard(sectionText) },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "Copy section",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        SelectionContainer {
            Column {
                section.items.forEach { item ->
                    item.value
                        ?.takeIf { value -> value.isNotBlank() }
                        ?.let { value ->
                            FerretKeyValueRow(
                                key = item.label,
                                value = value,
                            )
                        }
                }
            }
        }
    }
}
