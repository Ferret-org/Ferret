package com.ferret

import com.ferret.database.DatabaseFactory
import com.ferret.repository.NetworkRecordRepository
import com.ferret.repository.NetworkRecordRepositoryImpl


internal actual class FerretRepository() {

    private val database = DatabaseFactory.createDatabase()

    actual val networkRecordRepository: NetworkRecordRepository =
        NetworkRecordRepositoryImpl(database.networkRecordDao())
}
