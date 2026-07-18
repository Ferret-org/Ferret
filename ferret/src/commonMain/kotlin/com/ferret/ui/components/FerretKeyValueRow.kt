package com.ferret.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ferret.model.Header
import com.ferret.ui.theme.FerretTypography
import com.ferret.utils.copyToClipboard
import com.ferret.utils.formatBody

private const val MAX_BODY_PREVIEW_LENGTH = 100_000

@Composable
fun FerretKeyValueRow(
    key: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
    ) {
        Text(
            text = key,
            modifier = Modifier.weight(0.35f),
            style = FerretTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = value,
            modifier = Modifier.weight(0.65f),
            style = FerretTypography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun FerretSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier.padding(bottom = 12.dp),
        style = FerretTypography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun FerretHeadersCard(
    title: String,
    headers: List<Header>,
    modifier: Modifier = Modifier,
) {
    val headersText = remember(headers) {
        headers.joinToString("\n") { "${it.name}: ${it.value}" }
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
                text = "$title (${headers.size})",
                style = FerretTypography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = { copyToClipboard(headersText) },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "Copy headers",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        if (headers.isEmpty()) {
            Text(
                text = "No headers",
                style = FerretTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            SelectionContainer {
                Column {
                    headers.forEach { header ->
                        FerretKeyValueRow(
                            key = header.name,
                            value = header.value,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FerretBodyCard(
    title: String,
    body: String,
    contentType: String? = null,
    encoded: Boolean? = null,
    modifier: Modifier = Modifier,
) {
    val formattedBody = remember(body, contentType, encoded) {
        if (encoded == true) body
        else formatBody(body = body, contentType = contentType)
    }

    val displayBody = remember(formattedBody) {
        if (formattedBody.length > MAX_BODY_PREVIEW_LENGTH) {
            formattedBody.take(MAX_BODY_PREVIEW_LENGTH)
        } else {
            formattedBody
        }
    }

    val isTruncated = formattedBody.length > MAX_BODY_PREVIEW_LENGTH

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
                text = title,
                style = FerretTypography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = { copyToClipboard(formattedBody) },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "Copy body",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp)
                .horizontalScroll(rememberScrollState()),
        ) {
            SelectionContainer {
                Text(
                    text = displayBody,
                    style = FerretTypography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    color = MaterialTheme.colorScheme.onSurface,
                    softWrap = false,
                )
            }
        }

        if (isTruncated) {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = "Response truncated for preview",
                style = FerretTypography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
