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
        val js = "Hi, Team Ferret"
        wsManager.send(js)
    }

    override fun onCleared() {
        super.onCleared()
        wsManager.close()
    }

    companion object {
        private const val WS_URL = "wss://echo.websocket.org"
    }
}
