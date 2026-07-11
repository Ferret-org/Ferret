package com.ferret.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ferret.app.data.CourseRepository
import com.ferret.app.network.WebSocketManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: CourseRepository,
    private val wsManager: WebSocketManager,
) : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState = _appUiState
        .onStart { getAllArticle() }
        .stateIn(viewModelScope, SharingStarted.Lazily, AppUiState())

    init {
        viewModelScope.launch {
            wsManager.state.collect { wsState ->
                _appUiState.update { it.copy(wsState = wsState) }
            }
        }
        viewModelScope.launch {
            wsManager.messages.collect { message ->
                _appUiState.update { state ->
                    state.copy(wsMessages = (state.wsMessages + message).takeLast(5))
                }
                Logger.e("@@@@@") {
                    message.toString()
                }
            }
        }
    }

    fun getAllArticle() = viewModelScope.launch {
        _appUiState.update { it.copy(isLoading = true, error = null) }
        try {
            val courses = repository.getAllCourses()
            _appUiState.update {
                it.copy(isLoading = false, success = courses, error = null)
            }
        } catch (e: Exception) {
            _appUiState.update {
                it.copy(isLoading = false, error = e.message ?: "Something went wrong")
            }
        }
    }

    fun connectWebSocket() = wsManager.connect(WS_URL)

    fun disconnectWebSocket() = wsManager.disconnect()

    fun sendWsMessage() = viewModelScope.launch {
        val js =
            """{"header":{"destination_service":"greed-v2","internal_initiator":"","packet_name":"gruf","session_id":"Hi","source_service":"native","timestamp":1783538905630,"user_id":"25421","cv":"339"},"payload":{"type":"com.apps.common.model.socket.GetUserFeedPacketRequest","si":"","ecv":282,"wc":{"hwcf":130,"howcv":33,"hgwcv":11,"hswcv":14,"wwcv":6,"twcf":29},"bc":{"hfbcv":15,"hnbcv":70,"tbcv":6,"pvpbcv":8},"fc":{"agcv":-1,"pvpcv":82,"pvptcv":-1,"ptcv":-1,"abcv":-1,"scv":150,"psbscv":97,"mcv":57,"acv":-1,"faqcv":14,"scfaqcv":3,"xppcv":132,"ocv":2,"sccv":40},"pt":{"bsr":[]},"lbut":1783538490,"cs":"organic","cb":"game"}}""".trimIndent()
        wsManager.send(js)
    }

    override fun onCleared() {
        super.onCleared()
        wsManager.close()
    }

    companion object {
        //        private const val WS_URL = "wss://echo.websocket.org"
        private const val WS_URL =
            "wss://dev-tusk.bebetta.in/ws?token=eyJ0IjoiZXlKaGJHY2lPaUpJVXpJMU5pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SnBaQ0k2SWpJMU5UYzBJaXdpZG1WeWMybHZia052WkdVaU9qTXpPU3dpYVhOeklqb2lkSFZ6YXlJc0ltVjRjQ0k2TVRjNE16azFNVE0yTlN3aWJtSm1Jam94Tnpnek56YzROVFkxTENKcFlYUWlPakUzT0RNM056ZzFOalY5LnhNTDVRWnpxNlBlMXFscWpVeTFLNU1YLS1UbUhsVUZPZUdLRTZfa0p5cU0iLCJkIjoiZFptdjh0NDRUZXlfYXdIZ1NyalcybDpBUEE5MWJIbmUweVRsRjdPVnFiRVVzczRxN1ZkM2NhS25ySGRuREpSZHdEQ0JMb3p5bl83dGlOdnhpdFNCNFBoZzc4SkdRSWVzTDAwNnItUzR5bEdLeGNob19ZNnRhSGtaQ083alloQ3VBZUpDVEg4NDdwX1RlOCIsImN2IjoiMzM5IiwiYSI6Imh0dHBzOi8vbWVkaWEuYmViZXR0YS5pbi9wcm9maWxlX2ltYWdlcy9yb2JvdF8yNS53ZWJwIiwidSI6InVzZXJfdGh6em9hOHNoeDEifQ=="
    }
}
