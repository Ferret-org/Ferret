package com.ferret.common

import androidx.compose.ui.graphics.Color
import com.ferret.ui.theme.SemanticGray
import com.ferret.ui.theme.SemanticGrayBg
import com.ferret.ui.theme.SemanticGreen
import com.ferret.ui.theme.SemanticGreenBg
import com.ferret.ui.theme.SemanticOrange
import com.ferret.ui.theme.SemanticOrangeBg
import com.ferret.ui.theme.SemanticRed
import com.ferret.ui.theme.SemanticRedBg

enum class ConnectionState(val label: String, val fg: Color, val bg: Color) {
    CONNECTED("Connected", SemanticGreen, SemanticGreenBg),
    CLOSED("Closed", SemanticRed, SemanticRedBg),
    ERROR("Error", SemanticOrange, SemanticOrangeBg),
    IDLE("Closed", SemanticGray, SemanticGrayBg),
}

enum class FramePayloadType { TEXT, BINARY }

enum class MessageDirection { SENT, RECEIVED, SYSTEM, PING, PONG }