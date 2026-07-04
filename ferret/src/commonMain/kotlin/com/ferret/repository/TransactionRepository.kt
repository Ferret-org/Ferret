package com.ferret.repository

import com.ferret.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun insert(transaction: Transaction): Long

    suspend fun update(transaction: Transaction)

    suspend fun delete(transaction: Transaction)

    suspend fun clear()

    suspend fun get(id: Long): Transaction?

    suspend fun getAll(): List<Transaction>

    fun observeAll(): Flow<List<Transaction>>

    suspend fun deleteOlderThan(timestamp: Long)
}