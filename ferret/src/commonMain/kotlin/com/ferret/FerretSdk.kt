package com.ferret

import com.ferret.repository.NetworkRecordRepository

object FerretSdk {

    internal var repository: FerretRepository? = null

    internal val isInitialized: Boolean
        get() = repository != null

    val networkRecordRepository: NetworkRecordRepository
        get() = checkNotNull(repository) {
            "FerretSdk is not initialized. Call FerretSdk.initialize(context) first."
        }.networkRecordRepository

}

internal expect fun createRepository(
    configuration: FerretConfiguration
): FerretRepository
