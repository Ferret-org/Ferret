package com.ferret.ui


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ferret.ui.navigation.FerretNavigation

@Composable
fun FerretRoute(
    modifier: Modifier = Modifier,
) {
    FerretNavigation(
        modifier = modifier,
    )
}
