package com.ferret.usecase

import com.ferret.FerretConfiguration
import com.ferret.FerretSdk
import com.ferret.createRepository
import com.ferret.platform.bootServices

internal class InitializeFerretUseCase(
    private val configuration: FerretConfiguration
) {
    fun execute() {
        if (FerretSdk.repository != null) return
        bootServices(configuration)
        FerretSdk.repository = createRepository(configuration)
    }
}
