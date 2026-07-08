package com.ferret.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun FerretCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    border: BorderStroke? = BorderStroke(
        1.dp,
        MaterialTheme.colorScheme.outlineVariant
    ),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    elevation: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val body: @Composable () -> Unit = {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }

    if (onClick != null) {
        Surface(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            border = border,
            tonalElevation = elevation,
            content = body
        )
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            border = border,
            tonalElevation = elevation,
            content = body
        )
    }
}

@Preview
@Composable
private fun FerretCardStaticPreview() {
    MaterialTheme {
        Surface {
            FerretCard(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Static card — no onClick", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Preview
@Composable
private fun FerretCardClickablePreview() {
    MaterialTheme {
        Surface {
            FerretCard(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                onClick = {},
            ) {
                Text("Clickable card", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Preview
@Composable
private fun FerretCardBorderlessPreview() {
    MaterialTheme {
        Surface {
            FerretCard(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                border = null,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Text("Borderless, tinted card", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}