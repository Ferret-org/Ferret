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
        val jsonString = """
{"header":{"destination_service":"greed","source_service":"native","user_id":"25171","timestamp":1770056994630,"packet_name":"gw","session_id":"betta","cv":"339"}}
""".trimIndent()
        wsManager.send("ping")
    }

    override fun onCleared() {
        super.onCleared()
        wsManager.close()
    }

    companion object {
//        private const val WS_URL = "wss://dev-tusk.bebetta.in/ws?token=eyJ0IjoiZXlKaGJHY2lPaUpJVXpJMU5pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SnBaQ0k2SWpJMU1UY3hJaXdpZG1WeWMybHZia052WkdVaU9qTXpPU3dpYVhOeklqb2lkSFZ6YXlJc0ltVjRjQ0k2TVRjNE16VTVNRFV6TkN3aWJtSm1Jam94Tnpnek5ERTNOek0wTENKcFlYUWlPakUzT0RNME1UYzNNelI5LkxCZzdGSWU3OUUwdi1hbjJxOEw5alNFaFNFWHIxMVg4RWhpQmRMTXp4ZWMiLCJkIjoiZFptdjh0NDRUZXlfYXdIZ1NyalcybDpBUEE5MWJFVG5DZUx6ZUlOYW5QTmUxdElKTV95d19xam9CNGE1ZEdYM19xTGFWUkpPMGhOaElPYVAzXzFLbk9rU0NybUtyMkU0cTEzNURwYzBiT3F6RlVxZUZKM0RNZTR3Smx6bTdkelRUSGpRYUZzUEppSEZ0RSIsImN2IjoiMzM5IiwiYSI6IiIsInUiOiJzZWxlY3RpdmVfc2NhcmxldF9uYXRhIn0="
        private const val WS_URL = "wss://echo.websocket.org"
    }
}
