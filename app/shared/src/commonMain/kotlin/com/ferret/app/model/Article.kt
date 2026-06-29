package com.ferret.app.model

import androidx.compose.ui.graphics.Color

data class Article(
    val id: Int,
    val title: String,
    val subtitle: String,
    val author: String,
    val category: String,
    val readTimeMinutes: Int,
    val likes: Int,
    val accentColor: Color
)