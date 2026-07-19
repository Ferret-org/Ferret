package com.ferret.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ferret.ui.theme.FerretTypography

@Composable
fun FerretShareDialog(
    onShareCurl: () -> Unit,
    onShareOverview: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        FerretCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            elevation = 8.dp,
        ) {
            Text(
                text = "Share",
                style = FerretTypography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = "Choose what to share",
                style = FerretTypography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp),
            )

            FerretShareOptionCard(
                icon = Icons.Outlined.Terminal,
                title = "Share as cURL",
                subtitle = "Export the request as a runnable\nterminal command",
                iconContainerColor = { MaterialTheme.colorScheme.primaryContainer },
                iconTint = { MaterialTheme.colorScheme.onPrimaryContainer },
                onClick = {
                    onShareCurl()
                    onDismiss()
                },
            )

            Spacer(modifier = Modifier.height(10.dp))

            FerretShareOptionCard(
                icon = Icons.Outlined.Article,
                title = "Share Overview",
                subtitle = "Export a summary with headers\nand body content",
                iconContainerColor = { MaterialTheme.colorScheme.secondaryContainer },
                iconTint = { MaterialTheme.colorScheme.onSecondaryContainer },
                onClick = {
                    onShareOverview()
                    onDismiss()
                },
            )
        }
    }
}

@Composable
private fun FerretShareOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconContainerColor: @Composable () -> androidx.compose.ui.graphics.Color,
    iconTint: @Composable () -> androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    FerretCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = 0.dp,
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = iconContainerColor(),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = iconTint(),
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = title,
                    style = FerretTypography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = FerretTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}