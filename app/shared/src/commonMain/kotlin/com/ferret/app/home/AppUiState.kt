package com.ferret.app.home

import com.ferret.app.model.Article
import com.ferret.app.network.WebSocketManager

data class AppUiState(
    val isLoading: Boolean = false,
    val success: List<Article>? = null,
    val error: String? = null,
    val wsState: WebSocketManager.State = WebSocketManager.State.Idle,
    val wsMessages: List<String> = emptyList(),
)
