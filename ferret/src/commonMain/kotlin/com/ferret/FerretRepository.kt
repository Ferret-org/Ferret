package com.ferret

import com.ferret.repository.TransactionRepository


internal expect class FerretRepository {
    val transactionRepository: TransactionRepository
}