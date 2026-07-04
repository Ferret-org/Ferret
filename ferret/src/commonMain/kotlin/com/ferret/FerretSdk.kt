package com.ferret

import com.ferret.repository.TransactionRepository

object FerretSdk {

    internal var repository: FerretRepository? = null

    val transactionRepository: TransactionRepository?
        get() = repository?.transactionRepository
}

internal expect fun createRepository(
    configuration: FerretConfiguration
): FerretRepository
