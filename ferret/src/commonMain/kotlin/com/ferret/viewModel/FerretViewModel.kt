package com.ferret.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferret.FerretSdk
import com.ferret.common.FerretTab
import com.ferret.model.NetworkRecord
import com.ferret.repository.TransactionRepository
import com.ferret.usecase.ClearDatabaseUseCase
import com.ferret.usecase.GetTransactionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
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

class FerretViewModel(
    private val getTransactionUseCase: GetTransactionUseCase,
    private val clearDatabaseUseCase: ClearDatabaseUseCase,
) : ViewModel() {

    private val selectedTab = MutableStateFlow(FerretTab.ALL)

    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> =
        _searchQuery.asStateFlow()

    fun selectTab(tab: FerretTab) {
        selectedTab.value = tab
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    val ferretState = combine(
        getTransactionUseCase(),
        selectedTab,
        _searchQuery
            .debounce(300.milliseconds)
            .distinctUntilChanged(),
    ) { networkRecords, tab, query ->

        val filteredByTab = when (tab) {
            FerretTab.ALL -> {
                networkRecords
            }

            FerretTab.HTTP -> {
                networkRecords.filter {
                    !it.protocol.equals(
                        "WS",
                        ignoreCase = true,
                    )
                }
            }

            FerretTab.WEBSOCKET -> {
                networkRecords.filter {
                    it.protocol.equals(
                        "WS",
                        ignoreCase = true,
                    )
                }
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

        val sessions = filteredRecords
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

        FerretUiState(
            selectedTab = tab,
            searchQuery = query,
            sessions = sessions,
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
)

data class NetworkSession(
    val sessionId: String,
    val records: List<NetworkRecord>,
    val latestRequestDate: Long,
) {
    val recordCount: Int
        get() = records.size
}