package com.ferret.app.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ferret.app.components.AnimatedArticleCard
import com.ferret.app.components.CategoryChipItem
import com.ferret.app.components.FeaturedBannerCard
import com.ferret.app.components.rememberSmoothFlingBehavior
import com.ferret.app.model.ALL_CATEGORY
import com.ferret.app.model.categoryChips
import com.ferret.app.network.WebSocketManager
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    viewModel: AppViewModel = koinViewModel()
) {
    val state by viewModel.appUiState.collectAsStateWithLifecycle()
    AppContent(
        state = state,
        onConnectWs = { viewModel.connectWebSocket() },
        onDisconnectWs = { viewModel.disconnectWebSocket() },
        onSendWs = { viewModel.sendWsMessage() },
    )
}

@Composable
fun AppContent(
    state: AppUiState,
    onConnectWs: () -> Unit = {},
    onDisconnectWs: () -> Unit = {},
    onSendWs: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    val selectedChip = remember { mutableStateOf(ALL_CATEGORY) }

    val isScrolled by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 10 }
    }
    val topBarElevation by animateDpAsState(
        targetValue = if (isScrolled) 4.dp else 0.dp,
        animationSpec = tween(300),
        label = "topBarElevation"
    )

    val articles = state.success.orEmpty()
    val filteredArticles = remember(selectedChip.value, articles) {
        if (selectedChip.value == ALL_CATEGORY) articles
        else articles.filter { it.topic == selectedChip.value }
    }

    val smoothFling = rememberSmoothFlingBehavior()

    Scaffold(
        topBar = {
            Surface(shadowElevation = topBarElevation) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
                            )
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Ferret Sample",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 30.sp
                        )
                    )
                    Text(
                        text = "Curated articles for Android & iOS devs",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    )
                    Spacer(Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categoryChips.forEach { chip ->
                            CategoryChipItem(
                                chip = chip,
                                selected = selectedChip.value == chip.label,
                                onClick = { selectedChip.value = chip.label }
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        containerColor = Color(0xFFF0F2F5)
    ) { padding ->
        when {
            state.isLoading && articles.isEmpty() -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF6C63FF))
                }
            }

            state.error != null && articles.isEmpty() -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF6B7280)),
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }

            else -> {
                CompositionLocalProvider(LocalOverscrollFactory provides null) {
                    LazyColumn(
                        state = listState,
                        flingBehavior = smoothFling,
                        contentPadding = PaddingValues(
                            top = padding.calculateTopPadding() + 12.dp,
                            bottom = 32.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item(key = "websocket") {
                            WebSocketDemoCard(
                                state = state.wsState,
                                messages = state.wsMessages,
                                onConnect = onConnectWs,
                                onDisconnect = onDisconnectWs,
                                onSend = onSendWs,
                            )
                        }

                        item(key = "header") {
                            FeaturedBannerCard()
                        }

                        item(key = "section_label") {
                            Text(
                                text = "${filteredArticles.size} Articles",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Color(0xFF6B7280),
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                ),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                            )
                        }

                        itemsIndexed(
                            items = filteredArticles,
                            key = { _, article -> article.id }
                        ) { index, article ->
                            AnimatedArticleCard(article = article, index = index)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WebSocketDemoCard(
    state: WebSocketManager.State,
    messages: List<String>,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "WebSocket",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(wsStateColor(state))
                    )
                    Text(
                        text = wsStateLabel(state),
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF6B7280)),
                    )
                }
            }

            if (state is WebSocketManager.State.Connected) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = onDisconnect,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                        modifier = Modifier.weight(1f),
                    ) { Text("Disconnect") }
                    OutlinedButton(
                        onClick = onSend,
                        modifier = Modifier.weight(1f),
                    ) { Text("Send Ping") }
                }
            } else {
                Button(
                    onClick = onConnect,
                    enabled = state !is WebSocketManager.State.Connecting,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF)),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = if (state is WebSocketManager.State.Connecting) "Connecting…" else "Connect"
                    )
                }
            }

            if (state is WebSocketManager.State.Error) {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFEF4444)),
                )
            }

            if (messages.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Messages",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF6B7280)),
                    )
                    messages.forEach { msg ->
                        Text(
                            text = "← $msg",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xFF374151),
                        )
                    }
                }
            }
        }
    }
}

private fun wsStateColor(state: WebSocketManager.State): Color = when (state) {
    is WebSocketManager.State.Connected -> Color(0xFF22C55E)
    is WebSocketManager.State.Connecting -> Color(0xFFF59E0B)
    is WebSocketManager.State.Error -> Color(0xFFEF4444)
    else -> Color(0xFF9CA3AF)
}

private fun wsStateLabel(state: WebSocketManager.State): String = when (state) {
    WebSocketManager.State.Idle -> "Idle"
    WebSocketManager.State.Connecting -> "Connecting"
    WebSocketManager.State.Connected -> "Connected"
    WebSocketManager.State.Disconnected -> "Disconnected"
    is WebSocketManager.State.Error -> "Error"
}

@Preview
@Composable
fun AppPreview() {
    AppContent(state = AppUiState())
}
