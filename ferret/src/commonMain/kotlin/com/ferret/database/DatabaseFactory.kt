package com.ferret.database

internal expect object DatabaseFactory {

    fun createDatabase(
        context: Any
    ): FerretDatabase
}