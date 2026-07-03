package com.ferret.app.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferret.app.data.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: CourseRepository
) : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState = _appUiState
        .onStart { getAllArticle() }
        .stateIn(viewModelScope, SharingStarted.Lazily, AppUiState())

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
}
