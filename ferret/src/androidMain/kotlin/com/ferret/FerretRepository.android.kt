package com.ferret

import android.content.Context
import com.ferret.database.DatabaseFactory
import com.ferret.notification.NotificationConfiguration
import com.ferret.notification.NotificationKit
import com.ferret.repository.TransactionRepository
import com.ferret.repository.TransactionRepositoryImpl

internal actual class FerretRepository(
    context: Context,
    configuration: FerretConfiguration
) {

    private val database = DatabaseFactory.createDatabase(
        context.applicationContext
    )

    actual val transactionRepository: TransactionRepository =
        TransactionRepositoryImpl(
            database.transactionDao()
        )

    init {
        NotificationKit.boot(
            configuration = NotificationConfiguration(
                context = context.applicationContext,
                defaultSmallIcon = configuration.notifications.defaultSmallIcon,
                maxBufferSize = configuration.notifications.maxBufferSize,
                defaultPriority = configuration.notifications.defaultPriority,
                defaultChannel = configuration.notifications.defaultChannel
            )
        )
    }
}