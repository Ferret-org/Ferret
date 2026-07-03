package com.ferret.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ferret.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(entity: TransactionEntity): Long

    @Update
    suspend fun update(entity: TransactionEntity)

    @Delete
    suspend fun delete(entity: TransactionEntity)

    @Query("""
        SELECT *
        FROM transactions
        ORDER BY startTimestamp DESC
    """)
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query("""
        SELECT *
        FROM transactions
        WHERE id = :id
    """)
    suspend fun getById(id: Long): TransactionEntity?

    @Query("""
        SELECT *
        FROM transactions
        ORDER BY startTimestamp DESC
    """)
    suspend fun getAll(): List<TransactionEntity>

    @Query("DELETE FROM transactions")
    suspend fun clear()

    @Query("""
        DELETE FROM transactions
        WHERE startTimestamp < :timestamp
    """)
    suspend fun deleteOlderThan(timestamp: Long)
}