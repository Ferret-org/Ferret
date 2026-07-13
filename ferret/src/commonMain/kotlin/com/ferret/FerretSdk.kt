package com.ferret

import com.ferret.repository.TransactionRepository

object FerretSdk {

    internal var repository: FerretRepository? = null

    internal val isInitialized: Boolean
        get() = repository != null

    val transactionRepository: TransactionRepository
        get() = checkNotNull(repository) {
            "FerretSdk is not initialized. Call FerretSdk.initialize(context) first."
        }.transactionRepository

}

internal expect fun createRepository(
    configuration: FerretConfiguration
): FerretRepository
