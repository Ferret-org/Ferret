package com.ferret

import com.ferret.database.DatabaseFactory
import com.ferret.repository.NetworkRecordRepository
import com.ferret.repository.NetworkRecordRepositoryImpl

internal actual class FerretRepository(configuration: FerretConfiguration) {

    private val database = DatabaseFactory.createDatabase()

    actual val networkRecordRepository: NetworkRecordRepository =
        NetworkRecordRepositoryImpl(
            dao = database.networkRecordDao(),
            retentionDurationMs = configuration.retentionDurationHours * 60 * 60 * 1000,
        )
}
