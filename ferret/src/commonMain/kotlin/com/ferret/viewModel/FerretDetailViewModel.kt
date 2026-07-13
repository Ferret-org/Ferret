package com.ferret.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ferret.model.NetworkRecord
import com.ferret.ui.navigation.FerretDestination
import com.ferret.usecase.GetTransactionByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FerretDetailViewModel(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val networkId = savedStateHandle
        .toRoute<FerretDestination.NetworkDetail>()
        .networkId

    private val _ferretDetail = MutableStateFlow<NetworkRecord?>(null)
    val ferretDetail = _ferretDetail.onStart {
        loadTransaction()
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null,
    )

    private fun loadTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            _ferretDetail.value = getTransactionByIdUseCase(networkId)
        }
    }
}

