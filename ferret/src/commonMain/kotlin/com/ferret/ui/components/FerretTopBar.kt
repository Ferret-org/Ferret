package com.ferret.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FerretTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    titleContent: (@Composable () -> Unit)? = null,
    height: Dp = 56.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor,
        contentColor = contentColor,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .padding(horizontal = 8.dp),
            ) {
                if (navigationIcon != null) {
                    navigationIcon()
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight()
                        .padding(start = if (navigationIcon != null) 4.dp else 16.dp),
                ) {
                    if (titleContent != null) {
                        titleContent()
                    } else {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, content = actions)
            }

        }
    }
}


@Preview
@Composable
private fun FerretTopBarRootPreview() {
    MaterialTheme {
        FerretTopBar(
            title = "Ferret",
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Rounded.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                IconButton(onClick = {}) { Icon(Icons.Filled.Delete, contentDescription = "Clear") }
                IconButton(onClick = {}) { Icon(Icons.Filled.Settings, contentDescription = "Settings") }
            },
        )
    }
}

@Preview
@Composable
private fun FerretTopBarDetailWithSummaryPreview() {
    MaterialTheme {
        FerretTopBar(
            title = "GET /api/users/profile",
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = {}) { Icon(Icons.Filled.Share, contentDescription = "Share") }
                IconButton(onClick = {}) { Icon(Icons.Filled.MoreVert, contentDescription = "More") }
            },
        )
    }
}

@Preview
@Composable
private fun FerretTopBarSubtitlePreview() {
    MaterialTheme {
        FerretTopBar(
            title = "wss://ws.example.com/socket",
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
        )
    }
}