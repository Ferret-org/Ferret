package com.ferret.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ferret.model.Header
import com.ferret.ui.theme.FerretTypography
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
    FerretCard(
        modifier = modifier.fillMaxWidth(),
        elevation = 3.dp,
    ) {
        FerretSectionTitle(
            title = "$title (${headers.size})",
        )

        if (headers.isEmpty()) {
            Text(
                text = "No headers",
                style = FerretTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            headers.forEach { header ->
                FerretKeyValueRow(
                    key = header.name,
                    value = header.value,
                )
            }
        }
    }
}

@Composable
fun FerretBodyCard(
    title: String,
    body: String,
    contentType: String?,
    encoded: Boolean,
    modifier: Modifier = Modifier,
) {
    val formattedBody = remember(
        body,
        contentType,
        encoded,
    ) {
        if (encoded) {
            body
        } else {
            formatBody(
                body = body,
                contentType = contentType,
            )
        }
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
        elevation = 3.dp
    ) {
        FerretSectionTitle(
            title = title,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = displayBody,
            style = FerretTypography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            softWrap = true,
        )

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