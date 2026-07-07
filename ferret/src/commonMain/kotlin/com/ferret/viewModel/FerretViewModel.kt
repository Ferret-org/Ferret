package com.ferret.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferret.FerretSdk
import com.ferret.model.Transaction
import com.ferret.repository.TransactionRepository
import com.ferret.usecase.GetTransactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class FerretViewModel(
    private val getTransactionUseCase: GetTransactionUseCase
) : ViewModel() {

    val ferretState = getTransactionUseCase()
        .map { transactions ->
            FerretUiState(ferretList = transactions)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            FerretUiState()
        )

}


data class FerretUiState(
    val ferretList: List<Transaction>? = null,
)