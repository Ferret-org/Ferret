package com.ferret.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val FerretColorScheme = lightColorScheme(
    primary = FerretPurple,
    onPrimary = SurfaceWhite,
    secondary = FerretPurpleDark,
    background = SurfaceWhite,
    surface = SurfaceWhite,
    onBackground = Ink900,
    onSurface = Ink900,
    surfaceVariant = Ink50,
    outline = Ink100,
    error = SemanticRed,
)

@Composable
fun FerretTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FerretColorScheme,
        typography = FerretTypography,
        content = content,
    )
}
