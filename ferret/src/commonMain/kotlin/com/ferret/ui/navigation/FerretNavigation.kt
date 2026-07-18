package com.ferret.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun FerretNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    FerretNavGraph(
        navController = navController,
        modifier = modifier
    )
}