package com.ferret.app.di

import com.ferret.app.data.CourseRepository
import com.ferret.app.domain.CourseRepositoryImpl
import com.ferret.app.home.AppViewModel
import com.ferret.app.network.CoursesService
import com.ferret.app.network.WebSocketManager
import com.ferret.app.network.createCoursesService
import com.ferret.app.network.createHttpClient
import com.ferret.app.network.json
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/** Change this to point at the real courses backend. */
const val BASE_URL = "https://6a43fb586dba791499abb4b0.mockapi.io/api/v1/course/"

val networkModule = module {
    single<Json> { json }
    single<HttpClient> { createHttpClient(get()) }
    single<WebSocketManager> { WebSocketManager(get()) }
    single<Ktorfit> {
        Ktorfit.Builder()
            .baseUrl(BASE_URL)
            .httpClient(get<HttpClient>())
            .build()
    }
    single<CoursesService> { get<Ktorfit>().createCoursesService() }
}

val dataModule = module {
    single<CourseRepository> { CourseRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModelOf(::AppViewModel)
}

val appModules = listOf(networkModule, dataModule, viewModelModule)

/**
 * Starts Koin. Call this once on app startup. [appDeclaration] lets each
 * platform add its own definitions (e.g. `androidContext` on Android).
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(appModules)
}
