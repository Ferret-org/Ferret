package com.ferret

import android.content.Context
import com.ferret.database.DatabaseFactory
import com.ferret.repository.NetworkRecordRepository
import com.ferret.repository.NetworkRecordRepositoryImpl

internal actual class FerretRepository(
    context: Context,
    configuration: FerretConfiguration
) {

    private val database = DatabaseFactory.createDatabase()

    actual val networkRecordRepository: NetworkRecordRepository =
        NetworkRecordRepositoryImpl(database.networkRecordDao())
}