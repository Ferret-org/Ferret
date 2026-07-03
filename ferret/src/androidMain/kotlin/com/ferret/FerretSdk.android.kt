package com.ferret

import android.content.Context

internal actual fun createRepository(
    context: Any,
    configuration: FerretConfiguration
): FerretRepository {

    return FerretRepository(
        context = context as Context,
        configuration = configuration
    )
}