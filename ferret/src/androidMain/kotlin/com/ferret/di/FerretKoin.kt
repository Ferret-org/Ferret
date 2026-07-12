package com.ferret.di

import android.content.Context
import com.ferret.AndroidContextHolder
import com.ferret.FerretConfiguration
import com.ferret.platform.bootServices
import embedded.koin.android.ext.koin.androidContext
import embedded.koin.core.Koin
import embedded.koin.core.KoinApplication
import embedded.koin.dsl.koinApplication

internal object FerretKoin {
    private var koinApp: KoinApplication? = null

    val koin: Koin
        get() = koinApp?.koin ?: error("Ferret not initialized. Call FerretSdk.init() first.")

    fun start(context: Context, configuration: FerretConfiguration) {
        if (koinApp != null) return
        AndroidContextHolder.context = context.applicationContext
        koinApp = koinApplication {
            androidContext(context.applicationContext)
            modules(ferretModule(configuration))
        }
        bootServices(configuration)
    }
}
