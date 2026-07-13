package com.ferret

import com.ferret.database.DatabaseFactory
import com.ferret.repository.TransactionRepository
import com.ferret.repository.TransactionRepositoryImpl


internal actual class FerretRepository() {

    private val database = DatabaseFactory.createDatabase()

    actual val transactionRepository: TransactionRepository =
        TransactionRepositoryImpl(database.transactionDao())
}
