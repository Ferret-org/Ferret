package com.ferret

import android.content.Context
import com.ferret.database.DatabaseFactory
import com.ferret.repository.TransactionRepository
import com.ferret.repository.TransactionRepositoryImpl

internal actual class FerretRepository(
    context: Context,
    configuration: FerretConfiguration
) {

    private val database = DatabaseFactory.createDatabase()

    actual val transactionRepository: TransactionRepository =
        TransactionRepositoryImpl(database.transactionDao())
}