package com.ferret.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ferret.AndroidContextHolder
import kotlinx.coroutines.Dispatchers
import java.io.File

internal actual object DatabaseFactory {

    actual fun createDatabase(): FerretDatabase {
        val context = AndroidContextHolder.context
        val dbFile = File(context.filesDir, DatabaseConstants.DATABASE_NAME)

        return Room.databaseBuilder<FerretDatabase>(
            name = dbFile.absolutePath,
            context = context
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}
