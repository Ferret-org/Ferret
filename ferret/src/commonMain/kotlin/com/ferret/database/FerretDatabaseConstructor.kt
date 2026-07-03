package com.ferret.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object FerretDatabaseConstructor : RoomDatabaseConstructor<FerretDatabase> {
    override fun initialize(): FerretDatabase
}