package com.ferret.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview


enum class FerretBadgeType {
    Neutral,
    Blue,
    Green,
    Orange,
    Red,
    Purple,
}

@Composable
fun FerretBadge(
    text: String,
    type: FerretBadgeType = FerretBadgeType.Purple,
    modifier: Modifier = Modifier,
) {
    val color = when (type) {
        FerretBadgeType.Neutral -> MaterialTheme.colorScheme.onSurfaceVariant
        FerretBadgeType.Blue -> Color(0xFF2563EB)
        FerretBadgeType.Green -> Color(0xFF16A34A)
        FerretBadgeType.Orange -> Color(0xFFE07A1F)
        FerretBadgeType.Red -> Color(0xFFDC2626)
        FerretBadgeType.Purple -> Color(0xFF7C5CFC)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
private fun FerretBadgePreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FerretBadge("GET")
                    FerretBadge("POST")
                    FerretBadge("PUT")
                    FerretBadge("DELETE")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FerretBadge(
                        text = "Connected",
                    )
                    FerretBadge(
                        text = "Error",
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FerretBadge(
                        text = "200",
                    )
                    FerretBadge(
                        text = "500",
                    )
                }
            }
        }
    }
}