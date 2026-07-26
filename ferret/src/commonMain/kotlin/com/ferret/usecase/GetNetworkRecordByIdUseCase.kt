package com.ferret.usecase

import com.ferret.model.NetworkRecord
import com.ferret.repository.NetworkRecordRepository

class GetNetworkRecordByIdUseCase(
    private val repository: NetworkRecordRepository
) {
    suspend operator fun invoke(id: Long): NetworkRecord? {
        return repository.getById(id)
    }
}