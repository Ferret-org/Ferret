package com.ferret.database.converter

import androidx.room.TypeConverter
import com.ferret.model.Body
import com.ferret.model.Header
import com.ferret.model.TransactionProtocol
import com.ferret.model.TransactionState
import kotlinx.serialization.json.Json

object TransactionConverters {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun headersToJson(headers: List<Header>): String =
        json.encodeToString(headers)

    @TypeConverter
    fun jsonToHeaders(value: String): List<Header> =
        json.decodeFromString(value)

    @TypeConverter
    fun bodyToJson(body: Body?): String? =
        body?.let { json.encodeToString(it) }

    @TypeConverter
    fun jsonToBody(value: String?): Body? =
        value?.let { json.decodeFromString(it) }

    @TypeConverter
    fun protocolToString(protocol: TransactionProtocol): String =
        protocol.name

    @TypeConverter
    fun stringToProtocol(value: String): TransactionProtocol =
        TransactionProtocol.valueOf(value)

    @TypeConverter
    fun stateToString(state: TransactionState): String =
        state.name

    @TypeConverter
    fun stringToState(value: String): TransactionState =
        TransactionState.valueOf(value)

}