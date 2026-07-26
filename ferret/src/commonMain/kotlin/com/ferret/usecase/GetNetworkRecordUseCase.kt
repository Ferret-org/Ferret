package com.ferret.usecase

import com.ferret.repository.NetworkRecordRepository

class GetNetworkRecordUseCase(
    private val repository: NetworkRecordRepository
) {

    operator fun invoke()= repository.observeAll()

}
