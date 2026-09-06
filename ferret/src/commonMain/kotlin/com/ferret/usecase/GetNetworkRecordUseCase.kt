package com.ferret.usecase

import com.ferret.model.NetworkRecord
import com.ferret.repository.NetworkRecordRepository

class GetNetworkRecordUseCase(
    private val repository: NetworkRecordRepository
) {

    suspend operator fun invoke(limit: Int, offset: Int): List<NetworkRecord> {
        return repository.getPage(limit, offset)
    }

}
