package com.ferret.di

import com.ferret.FerretConfiguration
import com.ferret.database.DatabaseFactory
import com.ferret.database.FerretDatabase
import com.ferret.repository.TransactionRepository
import com.ferret.repository.TransactionRepositoryImpl
import com.ferret.usecase.ClearDatabaseUseCase
import com.ferret.usecase.GetTransactionUseCase
import com.ferret.usecase.SaveTransactionUseCase
import com.ferret.usecase.SaveWebSocketEventUseCase
import com.ferret.viewModel.FerretViewModel
import embedded.koin.androidx.viewmodel.dsl.viewModelOf
import embedded.koin.dsl.module

internal fun ferretModule(configuration: FerretConfiguration) = module {
    single { configuration }
    single { DatabaseFactory.createDatabase() }
    single<TransactionRepository> {
        TransactionRepositoryImpl(get<FerretDatabase>().transactionDao())
    }
    single { SaveTransactionUseCase(get()) }
    single { SaveWebSocketEventUseCase(get()) }
    single { GetTransactionUseCase(get()) }
    single { ClearDatabaseUseCase(get()) }
    viewModelOf(::FerretViewModel)
}
