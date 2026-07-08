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
import com.ferret.model.Transaction
import com.ferret.utils.formatBytes
import com.ferret.utils.formatTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@Composable
fun FerretNetworkCard(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    onClick: (Long?) -> Unit,
) {
    FerretCard(
        modifier = modifier,
        onClick = { onClick(transaction.id) },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                transaction.method?.let {
                    FerretBadge(
                        text = transaction.method ?: "",
                        type = it.badgeType(),
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = transaction.path,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (transaction.isResponseBodyEncoded) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            Spacer(Modifier.width(4.dp))
                        }
                        Text(
                            text = transaction.host,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )

                        if (transaction.isResponseBodyEncoded) {
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                        }
                    }

                }


                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    transaction.responseCode?.let {
                        Text(
                            text = it.toString(),
                            color = statusColor(it),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Text(
                        text = "${transaction.tookMs ?: "--"} ms",
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
                    text = transaction.requestDate.formatTime(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = transaction.requestPayloadSize.formatBytes(),
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
            transaction = Transaction(
                id = 1,
                sessionId = "session-1",
                requestDate = Clock.System.now().toEpochMilliseconds(),
                responseDate = Clock.System.now().toEpochMilliseconds() + 34,
                tookMs = 34,
                protocol = "HTTP/2",
                method = "GET",
                url = "https://api.example.com/api/users/profile",
                host = "api.example.com",
                path = "/api/users/profile",
                scheme = "https",
                responseTlsVersion = "TLS 1.3",
                responseCipherSuite = "TLS_AES_128_GCM_SHA256",
                requestPayloadSize = 1433,
                requestContentType = "application/json",
                requestHeaders = emptyList(),
                requestHeadersSize = 0,
                requestBody = null,
                isRequestBodyEncoded = false,
                responseCode = 200,
                responseMessage = "OK",
                error = null,
                responsePayloadSize = 1433,
                responseContentType = "application/json",
                responseHeaders = emptyList(),
                responseHeadersSize = 0,
                responseBody = """{"success":true}""",
                isResponseBodyEncoded = true,
            ),
            modifier = Modifier.padding(16.dp),
            onClick = {}
        )
    }
}