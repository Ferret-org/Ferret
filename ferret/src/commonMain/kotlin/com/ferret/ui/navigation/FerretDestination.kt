package com.ferret.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface FerretDestination {

    @Serializable
    data object NetworkList : FerretDestination

    @Serializable
    data class NetworkDetail(
        val networkId: Long,
    ) : FerretDestination
}