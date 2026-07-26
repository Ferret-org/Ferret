package com.ferret

internal actual fun createRepository(
    configuration: FerretConfiguration
): FerretRepository {
    return FerretRepository(configuration)
}