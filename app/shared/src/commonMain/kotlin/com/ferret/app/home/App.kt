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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    viewModel: AppViewModel = koinViewModel()
) {
    val state by viewModel.appUiState.collectAsStateWithLifecycle()
    AppContent(state = state)
}

@Composable
fun AppContent(
    state: AppUiState
) {

    val listState = rememberLazyListState()
    val selectedChip = remember { mutableStateOf(ALL_CATEGORY) }

    // Derive scroll offset for top-bar elevation animation
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

                    // Horizontal chip row (not lazy, just Row with horizontal scroll)
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
                // Disable the Android edge-glow overscroll effect — it fights the fling
                // and makes scrolling feel jerky on fast swipes.
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
                        // Featured header card
                        item(key = "header") {
                            FeaturedBannerCard()
                        }

                        // Section label
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

                        // Animated article cards
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

@Preview
@Composable
fun AppPreview() {
    AppContent(state = AppUiState())
}
