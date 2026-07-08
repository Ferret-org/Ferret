package com.ferret.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ferret.model.NetworkRecord
import com.ferret.utils.formatBytes
import com.ferret.utils.formatTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@Composable
fun FerretNetworkCard(
    id: Long,
    method: String,
    path: String,
    host: String,
    responseCode: Int,
    tookMs: Long,
    requestDate: Long,
    responsePayloadSize: Long,
    modifier: Modifier = Modifier,
    onClick: (Long?) -> Unit,
) {
    FerretCard(
        modifier = modifier,
        onClick = { onClick(id) },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                FerretBadge(
                    text = method,
                    type = method.badgeType(),
                )
                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = path,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = host,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }


                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = responseCode.toString(),
                        color = statusColor(responseCode),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = "${tookMs ?: "--"} ms",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }


            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = requestDate.formatTime(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = responsePayloadSize.formatBytes(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.width(12.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun String.badgeType(): FerretBadgeType =
    when (this.uppercase()) {
        "GET" -> FerretBadgeType.Green
        "POST" -> FerretBadgeType.Blue
        "PUT" -> FerretBadgeType.Orange
        "DELETE" -> FerretBadgeType.Red
        else -> FerretBadgeType.Neutral
    }

@Composable
private fun statusColor(code: Int): Color =
    when (code) {
        in 200..299 -> Color(0xFF16A34A)
        in 300..399 -> Color(0xFF2563EB)
        in 400..499 -> Color(0xFFE07A1F)
        else -> Color(0xFFDC2626)
    }



@Preview
@Composable
private fun FerretNetworkCardPreview() {
    MaterialTheme {
        FerretNetworkCard(
            modifier = Modifier.padding(16.dp),
            onClick = {},
            id = 1,
            method = "GET",
            path = "/api/users/profile",
            host = "api.example.com",
            responseCode = 200,
            tookMs = 34,
            requestDate = Clock.System.now().toEpochMilliseconds(),
            responsePayloadSize = 24,
        )
    }
}