package com.ferret.app.home

import com.ferret.app.model.Article

data class AppUiState(
    val isLoading: Boolean = false,
    val success: List<Article>? = null,
    val error: String? = null
)
