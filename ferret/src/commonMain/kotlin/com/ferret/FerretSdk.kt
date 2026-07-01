package com.ferret


object FerretSdk {

    private var repository: FerretRepository? = null
    fun initialize(
        context: Any,
        configuration: FerretConfiguration = FerretConfiguration()
    ) {
        if (repository != null) {
            error("FerretSdk has already been initialized.")
        }

        repository = createRepository(
            context,
            configuration
        )
    }
}

internal expect fun createRepository(
    context: Any,
    configuration: FerretConfiguration
): FerretRepository