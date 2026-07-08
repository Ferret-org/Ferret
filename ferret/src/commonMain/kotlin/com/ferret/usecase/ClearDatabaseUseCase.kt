package com.ferret.usecase

import com.ferret.repository.TransactionRepository

class ClearDatabaseUseCase(
    private val repository: TransactionRepository
) {

    suspend operator fun invoke()= repository.clear()

}