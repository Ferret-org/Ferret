package com.ferret.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import java.io.File

internal actual object DatabaseFactory {

    actual fun createDatabase(
        context: Any
    ): FerretDatabase {

        val dbFile = File(
            (context as Context).applicationContext.filesDir,
            DatabaseConstants.DATABASE_NAME
        )

        return Room.databaseBuilder<FerretDatabase>(
            name = dbFile.absolutePath,
            context = context
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}