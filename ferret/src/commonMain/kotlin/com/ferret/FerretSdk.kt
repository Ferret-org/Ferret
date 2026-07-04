package com.ferret

import com.ferret.notification.NotificationKit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

        NotificationKit.push {
            title("Ferret")
            message("Welcome")
        }
        repository?.transactionRepository?.let {
            CoroutineScope(Dispatchers.Default).launch {
                DatabaseTester.run(
                    it
                )
            }
        }

    }
}

internal expect fun createRepository(
    context: Any,
    configuration: FerretConfiguration
): FerretRepository