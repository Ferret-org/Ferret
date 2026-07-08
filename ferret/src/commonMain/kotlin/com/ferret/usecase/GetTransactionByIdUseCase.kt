package com.ferret.usecase

import com.ferret.model.NetworkRecord
import com.ferret.repository.TransactionRepository

class GetTransactionByIdUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Long): NetworkRecord? {
        return repository.getById(id)
    }
}