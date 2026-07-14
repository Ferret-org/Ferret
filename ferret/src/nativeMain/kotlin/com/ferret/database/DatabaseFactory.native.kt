package com.ferret.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual object DatabaseFactory {

    @OptIn(ExperimentalForeignApi::class)
    actual fun createDatabase(): FerretDatabase {

        val applicationSupportUrl = NSFileManager.defaultManager
            .URLForDirectory(
                directory = NSApplicationSupportDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = true,
                error = null,
            ) ?: error("Unable to resolve Application Support directory")

        val databasePath = "${applicationSupportUrl.path}/ferret.db"

        return Room.databaseBuilder<FerretDatabase>(
            name = databasePath,
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.Default)
            .build()
    }
}
