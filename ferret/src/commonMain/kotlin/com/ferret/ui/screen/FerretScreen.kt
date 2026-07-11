package com.ferret.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ferret.common.FerretTab
import com.ferret.ui.components.FerretNetworkCard
import com.ferret.ui.components.FerretSearchBar
import com.ferret.ui.theme.FerretTypography
import com.ferret.viewModel.FerretDetailViewModel
import com.ferret.viewModel.FerretViewModel

@Composable
fun FerretNetworkListScreen(
    modifier: Modifier = Modifier,
    ferretViewModel: FerretViewModel,
    onItemClick: (Long) -> Unit,
) {

    val ferretState by ferretViewModel.ferretState.collectAsStateWithLifecycle()
    val query by ferretViewModel.searchQuery.collectAsStateWithLifecycle()


    MaterialTheme {
        Scaffold(
            topBar = {
                Column {
                    FerretTopBar(
                        onDelete = ferretViewModel::clearDatabase // TODO Dialog()
                    )

                    FerretTabSelector(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        selectedTab = ferretState.selectedTab,
                        onTabSelected = ferretViewModel::selectTab
                    )

                    FerretSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        query = query,
                        onQueryChange = ferretViewModel::onSearchQueryChanged,
                        placeholder = when (ferretState.selectedTab) {
                            FerretTab.ALL -> "Search..."
                            FerretTab.HTTP -> "Search HTTP requests..."
                            FerretTab.WEBSOCKET -> "Search WebSocket connections..."
                        },
                        onFilterClick = {},
                        filterActive = ferretState.hasActiveFilters
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(
                        items = ferretState.ferretList,
                        key = { key -> key.id }
                    ) { ferretItem ->
                        FerretNetworkCard(
                            onClick = {
                                onItemClick(it)
                            },
                            id = ferretItem.id,
                            method = ferretItem.method ?: "",
                            path = ferretItem.path,
                            host = ferretItem.host,
                            responseCode = ferretItem.responseCode ?: 0,
                            tookMs = ferretItem.tookMs ?: 0,
                            requestDate = ferretItem.requestDate,
                            responsePayloadSize = ferretItem.responsePayloadSize,
                        )
                    }

                }
            }
        }
    }

}

@Composable
fun FerretTopBar(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Ferret",
            style = FerretTypography.bodyLarge
        )

        IconButton(
            onClick = {
                onDelete()
            },
            content = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            },
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FerretTabSelector(
    selectedTab: FerretTab,
    onTabSelected: (FerretTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth()
    ) {
        FerretTab.entries.forEachIndexed { index, tab ->
            SegmentedButton(
                modifier = Modifier.weight(1f),
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = FerretTab.entries.size
                ),
                label = {
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}
