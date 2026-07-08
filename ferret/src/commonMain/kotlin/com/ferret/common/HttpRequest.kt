package com.ferret.common

import androidx.compose.ui.graphics.Color
import com.ferret.ui.theme.SemanticBlue
import com.ferret.ui.theme.SemanticBlueBg
import com.ferret.ui.theme.SemanticGray
import com.ferret.ui.theme.SemanticGrayBg
import com.ferret.ui.theme.SemanticGreen
import com.ferret.ui.theme.SemanticGreenBg
import com.ferret.ui.theme.SemanticOrange
import com.ferret.ui.theme.SemanticOrangeBg
import com.ferret.ui.theme.SemanticRed
import com.ferret.ui.theme.SemanticRedBg

enum class HttpMethod(val label: String, val fg: Color, val bg: Color) {
    GET("GET", SemanticGreen, SemanticGreenBg),
    POST("POST", SemanticBlue, SemanticBlueBg),
    PUT("PUT", SemanticOrange, SemanticOrangeBg),
    DELETE("DELETE", SemanticRed, SemanticRedBg),
    PATCH("PATCH", SemanticGray, SemanticGrayBg),
}

enum class StatusFamily(val label: String) {
    INFO_1XX("1xx"), SUCCESS_2XX("2xx"), REDIRECT_3XX("3xx"),
    CLIENT_4XX("4xx"), SERVER_5XX("5xx");

    companion object {
        fun of(code: Int): StatusFamily = when (code / 100) {
            1 -> INFO_1XX; 2 -> SUCCESS_2XX; 3 -> REDIRECT_3XX
            4 -> CLIENT_4XX; else -> SERVER_5XX
        }
    }
}

fun statusColor(code: Int): Color = when (code / 100) {
    2 -> SemanticGreen
    3 -> SemanticBlue
    4 -> SemanticOrange
    else -> SemanticRed
}

