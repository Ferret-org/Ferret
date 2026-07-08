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
    private val clearDatabaseUseCase: ClearDatabaseUseCase
) : ViewModel() {

    private val selectedTab = MutableStateFlow(FerretTab.HTTP)

    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

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
            .distinctUntilChanged()
    ) { transactions, tab, query ->

        val filteredByTab = when (tab) {
            FerretTab.HTTP ->
                transactions.filter { !it.protocol.equals("WEBSOCKET", ignoreCase = true) }

            FerretTab.WEBSOCKET ->
                transactions.filter { it.protocol.equals("WEBSOCKET", ignoreCase = true) }
        }

        val filtered = if (query.isBlank()) {
            filteredByTab
        } else {
            filteredByTab.filter { transaction ->
                transaction.method.orEmpty().contains(query, ignoreCase = true) ||
                        transaction.host.contains(query, ignoreCase = true) ||
                        transaction.path.contains(query, ignoreCase = true) ||
                        transaction.url.contains(query, ignoreCase = true)
            }
        }

        FerretUiState(
            selectedTab = tab,
            searchQuery = query,
            ferretList = filtered
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        FerretUiState()
    )


    fun clearDatabase() = viewModelScope.launch(Dispatchers.IO) {
        clearDatabaseUseCase.invoke()
    }
}
data class FerretUiState(
    val selectedTab: FerretTab = FerretTab.HTTP,
    val ferretList: List<NetworkRecord> = emptyList(),
    val searchQuery: String = "",
    val hasActiveFilters: Boolean = false,
)