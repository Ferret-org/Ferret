package com.ferret.viewModel

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ferret.FerretSdk
import com.ferret.usecase.ClearDatabaseUseCase
import com.ferret.usecase.GetTransactionUseCase

val ferretViewModelFactory = viewModelFactory {
    initializer {
        FerretViewModel(
            GetTransactionUseCase(
                FerretSdk.transactionRepository
            ),
            ClearDatabaseUseCase(
                FerretSdk.transactionRepository
            )
        )
    }
}