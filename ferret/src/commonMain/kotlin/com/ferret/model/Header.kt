package com.ferret.model

import kotlinx.serialization.Serializable

@Serializable
data class Header(
    val name: String,
    val value: String
)