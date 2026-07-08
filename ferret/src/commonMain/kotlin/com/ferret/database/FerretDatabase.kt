package com.ferret.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ferret.database.converter.TransactionConverters
import com.ferret.database.dao.TransactionDao
import com.ferret.database.entity.NetworkRecordEntity

@Database(
    entities = [
        NetworkRecordEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(TransactionConverters::class)
@ConstructedBy(FerretDatabaseConstructor::class)
abstract class FerretDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
}