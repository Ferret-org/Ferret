package com.ferret.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferret.FerretSdk
import com.ferret.common.FerretTab
import com.ferret.model.NetworkRecord
import com.ferret.usecase.ClearDatabaseUseCase
import com.ferret.usecase.GetNetworkRecordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FerretViewModel(
    private val getTransactionUseCase: GetNetworkRecordUseCase,
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
) : ViewModel() {

    private companion object {
        const val PAGE_SIZE = 25
    }

    private val selectedTab = MutableStateFlow(FerretTab.ALL)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _records = MutableStateFlow<List<NetworkRecord>>(emptyList())

    private val _isLoading = MutableStateFlow(false)

    private val _hasMore = MutableStateFlow(true)

    private var offset = 0

    init {
        loadInitialRecords()
    }

    fun selectTab(tab: FerretTab) {
        if (selectedTab.value == tab) return

        selectedTab.value = tab
        resetPagination()
    }

    fun onSearchQueryChanged(query: String) {
        if (_searchQuery.value == query) return

        _searchQuery.value = query
        resetPagination()
    }

    private fun resetPagination() {
        offset = 0
        _records.value = emptyList()
        _hasMore.value = true

        loadInitialRecords()
    }

    private fun loadInitialRecords() {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val records = getTransactionUseCase(
                    limit = PAGE_SIZE,
                    offset = 0,
                )

                _records.value = records
                offset = records.size
                _hasMore.value = records.size == PAGE_SIZE
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMoreRecords() {
        if (_isLoading.value || !_hasMore.value) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val records = getTransactionUseCase(
                    limit = PAGE_SIZE,
                    offset = offset,
                )

                if (records.isEmpty()) {
                    _hasMore.value = false
                    return@launch
                }

                _records.update { current ->
                    current + records
                }

                offset += records.size

                _hasMore.value = records.size == PAGE_SIZE
            } finally {
                _isLoading.value = false
            }
        }
    }

    val ferretState: StateFlow<FerretUiState> = combine(
        _records,
        selectedTab,
        _searchQuery,
        _isLoading,
        _hasMore,
    ) { records, tab, query, isLoading, hasMore ->

        val filteredRecords = records.filter { record ->
            record.matches(
                tab = tab,
                query = query,
            )
        }

        val sessions = filteredRecords.groupBy { it.sessionId }.map { (sessionId, records) ->

            val sortedRecords = records.sortedByDescending { it.requestDate }

            NetworkSession(
                sessionId = sessionId,
                records = sortedRecords,
                latestRequestDate = sortedRecords.first().requestDate,
            )
        }.sortedByDescending {
            it.latestRequestDate
        }

        FerretUiState(
            selectedTab = tab,
            searchQuery = query,
            sessions = sessions,
            hasMore = hasMore,
            isLoading = isLoading,
        )
    }.onStart {
        emit(FerretUiState(isLoading = true))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FerretUiState(),
    )

    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            clearDatabaseUseCase()

            offset = 0
            _records.value = emptyList()
            _hasMore.value = false
        }
    }
}

private fun NetworkRecord.matches(
    tab: FerretTab,
    query: String,
): Boolean {

    val matchesTab = when (tab) {
        FerretTab.ALL -> true
        FerretTab.HTTP -> !isWebSocket
        FerretTab.WEBSOCKET -> isWebSocket
    }

    if (!matchesTab) return false

    if (query.isBlank()) return true

    return matchesQuery(query)
}

private fun NetworkRecord.matchesQuery(
    query: String,
): Boolean {
    return method.orEmpty().contains(query, ignoreCase = true) ||

            host.contains(query, ignoreCase = true) ||

            path.contains(query, ignoreCase = true) ||

            url.contains(query, ignoreCase = true) ||

            if (isWebSocket) {
                requestBody.orEmpty().contains(query, ignoreCase = true) ||
                        responseBody.orEmpty().contains(query, ignoreCase = true)
            } else {
                false
            }
}


data class FerretUiState(
    val selectedTab: FerretTab = FerretTab.ALL,
    val sessions: List<NetworkSession> = emptyList(),
    val searchQuery: String = "",
    val hasActiveFilters: Boolean = false,
    val hasMore: Boolean = false,
    val isLoading: Boolean = true,
)

data class NetworkSession(
    val sessionId: String,
    val records: List<NetworkRecord>,
    val latestRequestDate: Long,
) {
    val recordCount: Int
        get() = records.size
}