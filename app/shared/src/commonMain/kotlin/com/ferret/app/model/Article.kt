package com.ferret.app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: String,
    val title: String,
    val image: String,
    val author: String,
    val desc: String,
    val topic: String,
    @SerialName("createdAt") val createdAt: String
)