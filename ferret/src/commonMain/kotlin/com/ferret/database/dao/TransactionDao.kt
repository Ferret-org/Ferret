package com.ferret.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ferret.database.entity.NetworkRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(entity: NetworkRecordEntity): Long

    @Update
    suspend fun update(entity: NetworkRecordEntity)

    @Delete
    suspend fun delete(entity: NetworkRecordEntity)

    @Query("""
        UPDATE ${com.ferret.database.DatabaseConstants.TRANSACTIONS_TABLE}
        SET responseDate         = :responseDate,
            tookMs               = :tookMs,
            responseCode         = :responseCode,
            responseMessage      = :responseMessage,
            responsePayloadSize  = :responsePayloadSize,
            responseContentType  = :responseContentType,
            responseHeaders      = :responseHeaders,
            responseHeadersSize  = :responseHeadersSize,
            responseBody         = :responseBody,
            responseTlsVersion   = :responseTlsVersion,
            responseCipherSuite  = :responseCipherSuite
        WHERE sessionId = :sessionId
    """)
    suspend fun updateResponse(
        sessionId: String,
        responseDate: Long,
        tookMs: Long,
        responseCode: Int,
        responseMessage: String,
        responsePayloadSize: Long,
        responseContentType: String?,
        responseHeaders: String,
        responseHeadersSize: Int,
        responseBody: String?,
        responseTlsVersion: String?,
        responseCipherSuite: String?,
    )

    @Query("""
        UPDATE ${com.ferret.database.DatabaseConstants.TRANSACTIONS_TABLE}
        SET responseDate = :responseDate,
            tookMs       = :tookMs,
            error        = :error
        WHERE sessionId = :sessionId
    """)
    suspend fun updateError(
        sessionId: String,
        responseDate: Long,
        tookMs: Long,
        error: String?,
    )

    @Query("""
        UPDATE ${com.ferret.database.DatabaseConstants.TRANSACTIONS_TABLE}
        SET responseBody        = :responseBody,
            responsePayloadSize = :responsePayloadSize
        WHERE sessionId = :sessionId
    """)
    suspend fun updateWsFrameIn(sessionId: String, responseBody: String?, responsePayloadSize: Long)

    @Query("""
        UPDATE ${com.ferret.database.DatabaseConstants.TRANSACTIONS_TABLE}
        SET requestBody        = :requestBody,
            requestPayloadSize = :requestPayloadSize
        WHERE sessionId = :sessionId
    """)
    suspend fun updateWsFrameOut(sessionId: String, requestBody: String?, requestPayloadSize: Long)

    @Query("""
        UPDATE ${com.ferret.database.DatabaseConstants.TRANSACTIONS_TABLE}
        SET responseDate = :responseDate,
            tookMs       = :tookMs
        WHERE sessionId = :sessionId
    """)
    suspend fun updateWsClose(sessionId: String, responseDate: Long, tookMs: Long)

    @Query("DELETE FROM ${com.ferret.database.DatabaseConstants.TRANSACTIONS_TABLE}")
    suspend fun clear()

    @Query("""
        SELECT * FROM ${com.ferret.database.DatabaseConstants.TRANSACTIONS_TABLE}
        ORDER BY requestDate DESC
    """)
    fun observeAll(): Flow<List<NetworkRecordEntity>>

    @Query("""
        SELECT * FROM ${com.ferret.database.DatabaseConstants.TRANSACTIONS_TABLE}
        WHERE id = :id
    """)
    suspend fun getById(id: Long): NetworkRecordEntity?

    @Query("""
        SELECT * FROM ${com.ferret.database.DatabaseConstants.TRANSACTIONS_TABLE}
        ORDER BY requestDate DESC
    """)
    suspend fun getAll(): List<NetworkRecordEntity>

    @Query("""
        DELETE FROM ${com.ferret.database.DatabaseConstants.TRANSACTIONS_TABLE}
        WHERE requestDate < :timestamp
    """)
    suspend fun deleteOlderThan(timestamp: Long)
}
