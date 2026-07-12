package com.ferret.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ferret.ui.screen.FerretScreen
import com.ferret.viewModel.FerretViewModel

@Composable
fun FerretRoute(
    modifier: Modifier = Modifier,
    ferretViewModel: FerretViewModel,
) {
    FerretScreen(
        modifier = modifier,
        ferretViewModel = ferretViewModel,
    )
}
