package com.ferret.database.converter

import androidx.room.TypeConverter
import com.ferret.model.Header
import kotlinx.serialization.json.Json

object NetworkRecordConverters {

    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun headersToJson(headers: List<Header>): String =
        json.encodeToString(headers)

    @TypeConverter
    fun jsonToHeaders(value: String): List<Header> =
        json.decodeFromString(value)
}
