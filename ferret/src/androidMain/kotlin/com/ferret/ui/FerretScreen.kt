package com.ferret.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ferret.ui.components.FerretNetworkCard
import com.ferret.viewModel.FerretViewModel
import com.ferret.viewModel.ferretViewModelFactory

@Composable
fun FerretScreen(
    modifier: Modifier = Modifier,
    ferretViewModel: FerretViewModel = viewModel(
        factory = ferretViewModelFactory
    )
) {

    val ferretState by ferretViewModel.ferretState.collectAsStateWithLifecycle()

    MaterialTheme {
        Scaffold { paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    ferretState.ferretList?.let {
                        items(
                            items = it,
                            key = { key -> key.id }
                        ) { ferretItem ->
                            FerretNetworkCard(
                                transaction = ferretItem,
                                onClick =  {}
                            )
                        }
                    }

                }
            }
        }

    }



}