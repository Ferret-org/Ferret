package com.ferret.usecase

import com.ferret.model.Transaction
import com.ferret.repository.TransactionRepository

internal class SaveTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Long =
        repository.insert(transaction)

    suspend fun update(transaction: Transaction) =
        repository.update(transaction)
}
