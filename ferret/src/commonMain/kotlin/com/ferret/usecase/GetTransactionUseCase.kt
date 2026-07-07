package com.ferret.usecase

import com.ferret.repository.TransactionRepository

class GetTransactionUseCase(
    private val repository: TransactionRepository
) {

    operator fun invoke()= repository.observeAll()

}
