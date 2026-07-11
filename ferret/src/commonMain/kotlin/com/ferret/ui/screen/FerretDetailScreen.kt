package com.ferret.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ferret.common.FerretDetailTab
import com.ferret.model.NetworkRecord
import com.ferret.ui.components.FerretBodyCard
import com.ferret.ui.components.FerretCard
import com.ferret.ui.components.FerretHeadersCard
import com.ferret.ui.components.FerretKeyValueRow
import com.ferret.ui.components.FerretSectionTitle
import com.ferret.ui.components.FerretTopBar
import com.ferret.ui.theme.FerretTypography
import com.ferret.utils.formatBytes
import com.ferret.utils.formatTime
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
    val tabs = FerretDetailTab.entries

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size },
    )

    val coroutineScope = rememberCoroutineScope()

    MaterialTheme {
        Scaffold(
            topBar = {
                Column {
                    FerretTopBar(
                        modifier = Modifier.statusBarsPadding(),
                        titleContent = {
                            Text(
                                text = network?.path.orEmpty(),
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
                                onClick = {},
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
                    FerretDetailTab.OVERVIEW -> {
                        FerretOverviewContent(
                            network = network,
                        )
                    }

                    FerretDetailTab.REQUEST -> {
                        FerretRequestContent(
                            network = network,
                        )
                    }

                    FerretDetailTab.RESPONSE -> {
                        FerretResponseContent(
                            network = network,
                        )
                    }

                    FerretDetailTab.TIMING -> {
                        FerretTimingContent(
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
    tabs: List<FerretDetailTab>,
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
internal fun FerretOverviewContent(
    network: NetworkRecord?,
) {
    if (network == null) return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            FerretCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 3.dp,
            ) {
                FerretSectionTitle(
                    title = "General",
                )

                FerretKeyValueRow(
                    key = "URL",
                    value = network.url,
                )

                FerretKeyValueRow(
                    key = "Host",
                    value = network.host,
                )

                FerretKeyValueRow(
                    key = "Path",
                    value = network.path,
                )

                FerretKeyValueRow(
                    key = "Protocol",
                    value = network.protocol,
                )

                FerretKeyValueRow(
                    key = "Method",
                    value = network.method.orEmpty(),
                )

                FerretKeyValueRow(
                    key = "Started",
                    value = network.requestDate.formatTime(),
                )

                FerretKeyValueRow(
                    key = "Finished",
                    value = network.responseDate?.formatTime() ?: "",
                )

                FerretKeyValueRow(
                    key = "Duration",
                    value = network.tookMs
                        ?.let { "$it ms" }
                        ?: "—",
                )

                FerretKeyValueRow(
                    key = "TLS",
                    value = buildTlsInfo(network),
                )
            }
        }

        item {
            FerretCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 3.dp,
            ) {
                FerretSectionTitle(
                    title = "Sizes",
                )

                FerretKeyValueRow(
                    key = "Request Payload",
                    value = network.requestPayloadSize.formatBytes(),
                )

                FerretKeyValueRow(
                    key = "Response Payload",
                    value = network.responsePayloadSize.formatBytes(),
                )

                FerretKeyValueRow(
                    key = "Total Transfer",
                    value = (network.requestPayloadSize + network.responsePayloadSize).formatBytes(),
                )
            }
        }
    }
}

@Composable
internal fun FerretRequestContent(
    network: NetworkRecord?,
) {
    if (network == null) return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            FerretCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 3.dp,
            ) {
                FerretSectionTitle(
                    title = "Request",
                )

                FerretKeyValueRow(
                    key = "Method",
                    value = network.method.orEmpty(),
                )

                FerretKeyValueRow(
                    key = "URL",
                    value = network.url,
                )

                FerretKeyValueRow(
                    key = "Content Type",
                    value = network.requestContentType ?: "—",
                )

                FerretKeyValueRow(
                    key = "Payload Size",
                    value = network.requestPayloadSize.formatBytes(),
                )

                FerretKeyValueRow(
                    key = "Headers Size",
                    value = network.requestHeadersSize.toLong().formatBytes(),
                )

                FerretKeyValueRow(
                    key = "Encoded",
                    value = if (network.isRequestBodyEncoded) {
                        "Yes"
                    } else {
                        "No"
                    },
                )
            }
        }

        item {
            FerretHeadersCard(
                title = "Headers",
                headers = network.requestHeaders,
            )
        }

        network.requestBody?.let { body ->
            item {
                FerretBodyCard(
                    title = "Request Body",
                    body = body,
                    encoded = network.isRequestBodyEncoded,
                    contentType = network.requestContentType
                )
            }
        }
    }
}


@Composable
internal fun FerretResponseContent(
    network: NetworkRecord?,
) {
    if (network == null) return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            FerretCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 3.dp,
            ) {
                FerretSectionTitle(
                    title = "Response",
                )

                FerretKeyValueRow(
                    key = "Status",
                    value = buildString {
                        append(network.responseCode ?: "—")

                        network.responseMessage?.let {
                            append(" ")
                            append(it)
                        }
                    },
                )

                FerretKeyValueRow(
                    key = "Content Type",
                    value = network.responseContentType ?: "—",
                )

                FerretKeyValueRow(
                    key = "Payload Size",
                    value = network.responsePayloadSize.formatBytes(),
                )

                FerretKeyValueRow(
                    key = "Headers Size",
                    value = network.responseHeadersSize.toLong().formatBytes(),
                )

                FerretKeyValueRow(
                    key = "Encoded",
                    value = if (network.isResponseBodyEncoded) {
                        "Yes"
                    } else {
                        "No"
                    },
                )

                network.error?.let { error ->
                    FerretKeyValueRow(
                        key = "Error",
                        value = error,
                    )
                }
            }
        }

        item {
            FerretHeadersCard(
                title = "Headers",
                headers = network.responseHeaders,
            )
        }

        network.responseBody?.let { body ->
            item {
                FerretBodyCard(
                    title = "Response Body",
                    body = body,
                    encoded = network.isResponseBodyEncoded,
                    contentType = network.responseContentType,
                )
            }
        }
    }
}

@Composable
internal fun FerretTimingContent(
    network: NetworkRecord?,
) {
    if (network == null) return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            FerretCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 3.dp,
            ) {
                FerretSectionTitle(
                    title = "Timing",
                )

                FerretKeyValueRow(
                    key = "Request Started",
                    value = (network.requestDate).formatTime(),
                )

                FerretKeyValueRow(
                    key = "Response Finished",
                    value = network.responseDate?.formatTime() ?: "",
                )

                FerretKeyValueRow(
                    key = "Total Duration",
                    value = network.tookMs
                        ?.let { "$it ms" }
                        ?: "—",
                )
            }
        }
    }
}
private fun buildTlsInfo(
    network: NetworkRecord,
): String {
    val tlsVersion = network.responseTlsVersion
    val cipherSuite = network.responseCipherSuite

    return when {
        tlsVersion != null && cipherSuite != null -> {
            "$tlsVersion / $cipherSuite"
        }

        tlsVersion != null -> tlsVersion

        cipherSuite != null -> cipherSuite

        else -> "—"
    }
}