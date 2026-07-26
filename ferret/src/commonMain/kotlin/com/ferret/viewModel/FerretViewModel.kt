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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class FerretViewModel(
    private val getTransactionUseCase: GetNetworkRecordUseCase,
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
) : ViewModel() {

    private companion object {
        const val PAGE_SIZE = 5
    }

    private val selectedTab = MutableStateFlow(FerretTab.ALL)

    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> =
        _searchQuery.asStateFlow()

    private val visibleSessionCount = MutableStateFlow(PAGE_SIZE)

    fun selectTab(tab: FerretTab) {
        selectedTab.value = tab
        visibleSessionCount.value = PAGE_SIZE
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        visibleSessionCount.value = PAGE_SIZE
    }

    fun loadMoreSessions() {
        visibleSessionCount.value += PAGE_SIZE
    }

    @OptIn(FlowPreview::class)
    val ferretState = combine(
        getTransactionUseCase(),
        selectedTab,
        _searchQuery
            .debounce(300.milliseconds)
            .distinctUntilChanged(),
        visibleSessionCount,
    ) { networkRecords, tab, query, visibleCount ->

        val filteredByTab = when (tab) {
            FerretTab.ALL -> {
                networkRecords
            }

            FerretTab.HTTP -> {
                networkRecords.filter { !it.isWebSocket }
            }

            FerretTab.WEBSOCKET -> {
                networkRecords.filter { it.isWebSocket }
            }
        }

        val filteredRecords = if (query.isBlank()) {
            filteredByTab
        } else {
            filteredByTab.filter { networkRecord ->
                networkRecord.method
                    .orEmpty()
                    .contains(query, ignoreCase = true) ||
                        networkRecord.host
                            .contains(query, ignoreCase = true) ||
                        networkRecord.path
                            .contains(query, ignoreCase = true) ||
                        networkRecord.url
                            .contains(query, ignoreCase = true)
            }
        }

        val allSessions = filteredRecords
            .groupBy { it.sessionId }
            .map { (sessionId, records) ->
                val sortedRecords = records.sortedByDescending {
                    it.requestDate
                }

                NetworkSession(
                    sessionId = sessionId,
                    records = sortedRecords,
                    latestRequestDate = sortedRecords
                        .first()
                        .requestDate,
                )
            }
            .sortedByDescending {
                it.latestRequestDate
            }

        val pagedSessions = allSessions.take(visibleCount)

        FerretUiState(
            selectedTab = tab,
            searchQuery = query,
            sessions = pagedSessions,
            hasMore = allSessions.size > pagedSessions.size,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FerretUiState(),
    )

    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            clearDatabaseUseCase()
        }
    }
}

data class FerretUiState(
    val selectedTab: FerretTab = FerretTab.ALL,
    val sessions: List<NetworkSession> = emptyList(),
    val searchQuery: String = "",
    val hasActiveFilters: Boolean = false,
    val hasMore: Boolean = false,
)

data class NetworkSession(
    val sessionId: String,
    val records: List<NetworkRecord>,
    val latestRequestDate: Long,
) {
    val recordCount: Int
        get() = records.size
}