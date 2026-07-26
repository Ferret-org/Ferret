package com.ferret.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ferret.database.converter.NetworkRecordConverters
import com.ferret.database.dao.NetworkRecordDao
import com.ferret.database.entity.NetworkRecordEntity

@Database(
    entities = [
        NetworkRecordEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(NetworkRecordConverters::class)
@ConstructedBy(FerretDatabaseConstructor::class)
abstract class FerretDatabase : RoomDatabase() {

    abstract fun networkRecordDao(): NetworkRecordDao
}
