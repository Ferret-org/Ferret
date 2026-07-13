package com.ferret.viewModel

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ferret.FerretSdk
import com.ferret.usecase.GetTransactionByIdUseCase

val ferretDetailViewModelFactory = viewModelFactory {
    initializer {
        FerretDetailViewModel(
            GetTransactionByIdUseCase(
                FerretSdk.transactionRepository
            ),
            savedStateHandle = createSavedStateHandle()
        )
    }
}