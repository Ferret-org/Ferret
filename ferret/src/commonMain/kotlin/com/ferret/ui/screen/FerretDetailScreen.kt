package com.ferret.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ferret.common.FerretNetworkDetailTab
import com.ferret.model.NetworkRecord
import com.ferret.ui.components.FerretBodyCard
import com.ferret.ui.components.FerretDetailContent
import com.ferret.ui.components.FerretHeadersCard
import com.ferret.ui.components.FerretShareDialog
import com.ferret.ui.components.FerretTopBar
import com.ferret.ui.mapper.toHttpOverviewSections
import com.ferret.ui.mapper.toRequestSections
import com.ferret.ui.mapper.toResponseSections
import com.ferret.ui.mapper.toTimingSections
import com.ferret.ui.mapper.toWebSocketOverviewSections
import com.ferret.ui.theme.FerretTypography
import com.ferret.utils.shareText
import com.ferret.utils.toCurlCommand
import com.ferret.utils.toShareText
import com.ferret.viewModel.FerretDetailViewModel
import kotlinx.coroutines.launch

@Composable
fun FerretDetailScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    ferretDetailViewModel: FerretDetailViewModel,
) {
    val network by ferretDetailViewModel
        .ferretDetail
        .collectAsStateWithLifecycle()

    FerretDetailScreenContent(
        modifier = modifier,
        network = network,
        onNavigateBack = onNavigateBack,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FerretDetailScreenContent(
    modifier: Modifier = Modifier,
    network: NetworkRecord?,
    onNavigateBack: () -> Unit,
) {

    if (network == null) return

    var showShareDialog by rememberSaveable { mutableStateOf(false) }

    val tabs: List<FerretNetworkDetailTab> =
        if (network.isWebSocket) {
            FerretNetworkDetailTab.WebSocket.entries
        } else {
            FerretNetworkDetailTab.Http.entries
        }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size },
    )

    val coroutineScope = rememberCoroutineScope()

    if (showShareDialog) {
        FerretShareDialog(
            onShareCurl = { shareText(network.toCurlCommand()) },
            onShareOverview = { shareText(network.toShareText()) },
            onDismiss = { showShareDialog = false },
        )
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                Column {
                    FerretTopBar(
                        modifier = Modifier.statusBarsPadding(),
                        titleContent = {
                            Text(
                                text = network.path,
                                style = FerretTypography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = onNavigateBack,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { showShareDialog = true },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                )
                            }
                        },
                    )

                    HorizontalDivider(
                        modifier = Modifier.height(1.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )
                    FerretDetailTabSelector(
                        pagerState = pagerState,
                        tabs = tabs,
                        onTabSelected = { index ->
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    )
                    HorizontalDivider(
                        modifier = Modifier.height(1.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )
                }
            },
        ) { paddingValues ->
            HorizontalPager(
                state = pagerState,
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 8.dp),
                key = { page ->
                    tabs[page]
                },
                beyondViewportPageCount = 1,
            ) { page ->
                when (tabs[page]) {

                    FerretNetworkDetailTab.Http.OVERVIEW -> {
                        FerretHttpOverviewContent(
                            network = network,
                        )
                    }

                    FerretNetworkDetailTab.Http.REQUEST -> {
                        FerretRequestContent(
                            network = network,
                        )
                    }

                    FerretNetworkDetailTab.Http.RESPONSE -> {
                        FerretResponseContent(
                            network = network,
                        )
                    }

                    FerretNetworkDetailTab.Http.TIMING -> {
                        FerretTimingContent(
                            network = network,
                        )
                    }

                    FerretNetworkDetailTab.WebSocket.OVERVIEW -> {
                        FerretWebSocketOverviewContent(
                            network = network,
                        )
                    }

                    FerretNetworkDetailTab.WebSocket.REQUEST -> {
                        FerretRequestContent(
                            network = network,
                        )
                    }

                    FerretNetworkDetailTab.WebSocket.RESPONSE -> {
                        FerretResponseContent(
                            network = network,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FerretDetailTabSelector(
    pagerState: PagerState,
    tabs: List<FerretNetworkDetailTab>,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            tabs.forEachIndexed { index, tab ->
                FerretDetailTabItem(
                    modifier = Modifier.weight(1f),
                    title = tab.title,
                    selected = pagerState.currentPage == index,
                    onClick = {
                        onTabSelected(index)
                    },
                )
            }
        }

        FerretPagerIndicator(
            pagerState = pagerState,
            tabCount = tabs.size,
            modifier = Modifier.align(Alignment.BottomStart),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FerretPagerIndicator(
    pagerState: PagerState,
    tabCount: Int,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
    ) {
        val tabWidth = maxWidth / tabCount

        val indicatorOffset =
            tabWidth * (pagerState.currentPage + pagerState.currentPageOffsetFraction)

        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(tabWidth)
                .height(1.5.dp)
                .background(MaterialTheme.colorScheme.primary),
        )
    }
}

@Composable
private fun FerretDetailTabItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = FerretTypography.titleSmall,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
        )
    }
}

@Composable
internal fun FerretHttpOverviewContent(
    network: NetworkRecord,
) {
    FerretDetailContent(
        sections = network.toHttpOverviewSections(),
    )
}

@Composable
internal fun FerretRequestContent(
    network: NetworkRecord,
) {
    FerretDetailContent(
        sections = network.toRequestSections(),
        additionalContent = {
            if (network.requestHeaders.isNotEmpty()) {
                item {
                    FerretHeadersCard(
                        title = "Headers",
                        headers = network.requestHeaders,
                    )
                }
            }
            network.requestBody
                ?.takeIf { body ->
                    body.isNotBlank()
                }
                ?.let { body ->
                    item {
                        FerretBodyCard(
                            title = "Request Body",
                            body = body,
                            encoded = network.isRequestBodyEncoded,
                            contentType = network.requestContentType,
                        )
                    }
                }
        },
    )
}


@Composable
internal fun FerretResponseContent(
    network: NetworkRecord,
) {
    FerretDetailContent(
        sections = network.toResponseSections(),
        additionalContent = {
            if (network.responseHeaders.isNotEmpty()) {
                item {
                    FerretHeadersCard(
                        title = "Headers",
                        headers = network.responseHeaders,
                    )
                }
            }

            network.responseBody
                ?.takeIf { body ->
                    body.isNotBlank()
                }
                ?.let { body ->
                    item {
                        FerretBodyCard(
                            title = "Response Body",
                            body = body,
                            encoded = network.isResponseBodyEncoded,
                            contentType = network.responseContentType,
                        )
                    }
                }
        },
    )
}

@Composable
internal fun FerretTimingContent(
    network: NetworkRecord,
) {
    FerretDetailContent(
        sections = network.toTimingSections(),
    )
}

@Composable
internal fun FerretWebSocketOverviewContent(
    network: NetworkRecord,
) {
    FerretDetailContent(
        sections = network.toWebSocketOverviewSections(),
    )
}