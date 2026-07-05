package com.ferret.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual object DatabaseFactory {

    @OptIn(ExperimentalForeignApi::class)
    actual fun createDatabase(): FerretDatabase {

        val documentsPath = NSFileManager.defaultManager
            .URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            )!!.path!!

        val dbPath = "$documentsPath/ferret.db"

        return Room.databaseBuilder<FerretDatabase>(
            name = dbPath
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.Default)
            .build()
    }
}