package com.ferret.viewModel

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ferret.FerretSdk
import com.ferret.usecase.ClearDatabaseUseCase
import com.ferret.usecase.GetNetworkRecordUseCase

val ferretViewModelFactory = viewModelFactory {
    initializer {
        FerretViewModel(
            GetNetworkRecordUseCase(
                FerretSdk.networkRecordRepository
            ),
            ClearDatabaseUseCase(
                FerretSdk.networkRecordRepository
            )
        )
    }
}