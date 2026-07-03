package com.ferret.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ferret.database.DatabaseConstants
import com.ferret.model.Body
import com.ferret.model.Header
import com.ferret.model.HttpMethod
import com.ferret.model.TransactionProtocol
import com.ferret.model.TransactionState

@Entity(
    tableName = DatabaseConstants.TRANSACTIONS_TABLE,
    indices = [
        Index("sessionId"),
        Index("protocol"),
        Index("state"),
        Index("startTimestamp")
    ]
)
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val sessionId: String,

    val protocol: TransactionProtocol,

    val state: TransactionState,

    val url: String,

    val method: HttpMethod?,

    val requestHeaders: List<Header>,

    val responseHeaders: List<Header>,

    val requestBody: Body?,

    val responseBody: Body?,

    val statusCode: Int?,

    val startTimestamp: Long,

    val endTimestamp: Long?,

    val durationMs: Long?,

    val isSecure: Boolean,

    val errorMessage: String?
)