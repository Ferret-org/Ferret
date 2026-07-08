package com.ferret.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ferret.common.FerretTab
import com.ferret.ui.components.FerretNetworkCard
import com.ferret.ui.components.FerretSearchBar
import com.ferret.ui.screen.FerretScreen
import com.ferret.ui.theme.FerretTypography
import com.ferret.viewModel.FerretViewModel
import com.ferret.viewModel.ferretViewModelFactory

@Composable
fun FerretRoute(
    modifier: Modifier = Modifier,
    ferretViewModel: FerretViewModel = viewModel(
        factory = ferretViewModelFactory
    )
) {
    FerretScreen(
        modifier = modifier,
        ferretViewModel = ferretViewModel,
    )
}
