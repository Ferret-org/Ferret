package com.ferret.model

import kotlinx.serialization.Serializable

@Serializable
data class Body(
    val contentType: String? = null,
    val content: String? = null,
    val sizeInBytes: Long = 0L,
    val isEncoded: Boolean = false
)