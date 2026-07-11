package com.ferret.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ferret.ui.screen.FerretDetailScreen
import com.ferret.ui.screen.FerretNetworkListScreen
import com.ferret.viewModel.FerretDetailViewModel
import com.ferret.viewModel.FerretViewModel
import com.ferret.viewModel.ferretDetailViewModelFactory
import com.ferret.viewModel.ferretViewModelFactory

@Composable
internal fun FerretNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = FerretDestination.NetworkList,
    ) {
        composable<FerretDestination.NetworkList> {
            val ferretViewModel =
                viewModel<FerretViewModel>(
                    factory = ferretViewModelFactory
                )
            FerretNetworkListScreen(
                ferretViewModel = ferretViewModel,
                onItemClick = { networkId ->
                    navController.navigate(
                        FerretDestination.NetworkDetail(
                            networkId = networkId,
                        )
                    )
                },
            )
        }

        composable<FerretDestination.NetworkDetail> {
            val ferretDetailViewModel =
                viewModel<FerretDetailViewModel>(
                    factory = ferretDetailViewModelFactory,
                )
            FerretDetailScreen(
                ferretDetailViewModel = ferretDetailViewModel,
                onNavigateBack = navController::popBackStack,
            )
        }
    }
}